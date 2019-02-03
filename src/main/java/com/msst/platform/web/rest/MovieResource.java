package com.msst.platform.web.rest;
import com.msst.platform.domain.Movie;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.facade.MsstPlatformFacade;
import com.msst.platform.service.MovieService;
import com.msst.platform.service.dto.MovieInfo;
import com.msst.platform.web.rest.errors.BadRequestAlertException;
import com.msst.platform.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MovieInfo.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private static final String ENTITY_NAME = "movie";

    private final MovieService movieService;
    private final MsstPlatformFacade platformFacade;

    public MovieResource(MovieService movieService, MsstPlatformFacade platformFacade) {
        this.movieService = movieService;
        this.platformFacade = platformFacade;
    }

    /**
     * POST  /movies : Create a new movie.
     *
     * @param movie the movie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movie, or with status 400 (Bad Request) if the movie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to save MovieInfo : {}", movie);
        if (movie.getId() != null) {
            throw new BadRequestAlertException("A new movie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Movie result = movieService.save(movie);
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movies : Updates an existing movie.
     *
     * @param movie the movie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movie,
     * or with status 400 (Bad Request) if the movie is not valid,
     * or with status 500 (Internal Server Error) if the movie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movies")
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to update MovieInfo : {}", movie);
        if (movie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Movie result = movieService.save(movie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, movie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movies : get all the movies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of movies in body
     */
    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        log.debug("REST request to get all Movies");
        return movieService.findAll();
    }

    @GetMapping("/movieInfos")
    public List<MovieInfo> getAllMovieInfos() {
      log.debug("REST request to get all MovieInfo's info");
      return platformFacade.getAllMovieInfos();
    }

    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movie, or with status 404 (Not Found)
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable String id) {
        log.debug("REST request to get MovieInfo : {}", id);
        Optional<Movie> movie = movieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movie);
    }

    /**
     * DELETE  /movies/:id : delete the "id" movie.
     *
     * @param id the id of the movie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        log.debug("REST request to delete MovieInfo : {}", id);
        movieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/movies/{id}/subtitles")
    public ResponseEntity<Collection<Subtitle>> getMovieSubtitles(@PathVariable String id) {
        log.debug("REST request to get subtitles for movie with MovieInfo : '{}'", id);
        Optional<Movie> movie = movieService.findOne(id);
        return movie.<ResponseEntity<Collection<Subtitle>>>map(movie1 -> ResponseEntity.ok(movie1.getSubtitles()))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
