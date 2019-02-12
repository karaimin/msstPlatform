package com.msst.platform.web.rest;

import com.msst.platform.MsstPlatformApp;
import com.msst.platform.domain.User;
import com.msst.platform.repository.reactive.UserRepository;
import com.msst.platform.security.jwt.TokenProvider;
import com.msst.platform.web.rest.errors.ExceptionTranslator;
import com.msst.platform.web.rest.vm.LoginVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Test class for the UserJWTController REST controller.
 *
 * @see UserJWTController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsstPlatformApp.class)
public class UserJWTControllerIntTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private WebTestClient webTestClient;

    @Before
    public void setup() {
        UserJWTController userJWTController = new UserJWTController(tokenProvider, authenticationManager);
        this.webTestClient = WebTestClient.bindToController(userJWTController)
            .controllerAdvice(exceptionTranslator)
            .build();
    }

    @Test
    public void testAuthorize() throws Exception {
        User user = new User();
        user.setLogin("user-jwt-controller");
        user.setEmail("user-jwt-controller@example.com");
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));

        userRepository.save(user).block();

        LoginVM login = new LoginVM();
        login.setUsername("user-jwt-controller");
        login.setPassword("test");
        webTestClient.post().uri("/api/authenticate")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .syncBody(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("$.id_token").isNotEmpty();
    }

    @Test
    public void testAuthorizeWithRememberMe() throws Exception {
        User user = new User();
        user.setLogin("user-jwt-controller-remember-me");
        user.setEmail("user-jwt-controller-remember-me@example.com");
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));

        userRepository.save(user).block();

        LoginVM login = new LoginVM();
        login.setUsername("user-jwt-controller-remember-me");
        login.setPassword("test");
        login.setRememberMe(true);
        webTestClient.post().uri("/api/authenticate")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .syncBody(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("$.id_token").isNotEmpty();
    }

    @Test
    public void testAuthorizeFails() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername("wrong-user");
        login.setPassword("wrong password");
        webTestClient.post().uri("/api/authenticate")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .syncBody(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isUnauthorized()
            .expectHeader().doesNotExist("Authorization")
            .expectBody()
            .jsonPath("$.id_token").doesNotExist();
    }
}
