package com.msst.platform.web.rest;

import com.msst.platform.config.Constants;
import com.msst.platform.domain.User;
import com.msst.platform.repository.reactive.UserRepository;
import com.msst.platform.security.AuthoritiesConstants;
import com.msst.platform.service.MailService;
import com.msst.platform.service.UserService;
import com.msst.platform.service.dto.UserDTO;
import com.msst.platform.web.rest.errors.BadRequestAlertException;
import com.msst.platform.web.rest.errors.EmailAlreadyUsedException;
import com.msst.platform.web.rest.errors.LoginAlreadyUsedException;
import com.msst.platform.web.rest.util.HeaderUtil;
import com.msst.platform.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        }
        return userRepository.findOneByLogin(userDTO.getLogin().toLowerCase())
            .hasElement()
            .flatMap(loginExists -> {
                if (loginExists) {
                    throw new LoginAlreadyUsedException();
                }
                return userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
            })
            .hasElement()
            .flatMap(emailExists -> {
                if (emailExists) {
                    throw new EmailAlreadyUsedException();
                }
                return userService.createUser(userDTO);
            })
            .doOnSuccess(mailService::sendCreationEmail)
            .map(user -> {
                try {
                    return ResponseEntity.created(new URI("/api/users/" + user.getLogin()))
                        .headers(HeaderUtil.createAlert( "userManagement.created", user.getLogin()))
                        .body(user);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<ResponseEntity<UserDTO>> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        return userRepository.findOneByEmailIgnoreCase(userDTO.getEmail())
            .filter(user -> !user.getId().equals(userDTO.getId()))
            .hasElement()
            .flatMap(emailExists -> {
                if (emailExists) {
                    throw new EmailAlreadyUsedException();
                }
                return userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
            })
            .filter(user -> !user.getId().equals(userDTO.getId()))
            .hasElement()
            .flatMap(loginExists -> {
                if (loginExists) {
                    throw new LoginAlreadyUsedException();
                }
                return userService.updateUser(userDTO);
            })
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(user -> ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()))
                .body(user)
            );
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    public Mono<ResponseEntity<Flux<UserDTO>>> getAllUsers(Pageable pageable) {
        return userService.countManagedUsers()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(page, "/api/users"))
            .map(headers -> ResponseEntity.ok().headers(headers).body(userService.getAllManagedUsers(pageable)));
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<List<String>> getAuthorities() {
        return userService.getAuthorities().collectList();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public Mono<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
            .map(UserDTO::new)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        return userService.deleteUser(login)
            .map(it -> ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build());
    }
}
