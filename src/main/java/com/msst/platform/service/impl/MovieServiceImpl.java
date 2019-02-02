package com.msst.platform.service.impl;

import com.msst.platform.service.MovieService;
import com.msst.platform.domain.Movie;
import com.msst.platform.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Movie.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Save a movie.
     *
     * @param movie the entity to save
     * @return the persisted entity
     */
    @Override
    public Movie save(Movie movie) {
        log.debug("Request to save Movie : {}", movie);
        return movieRepository.save(movie);
    }

    /**
     * Get all the movies.
     *
     * @return the list of entities
     */
    @Override
    public List<Movie> findAll() {
        log.debug("Request to get all Movies");
        return movieRepository.findAll();
    }


    /**
     * Get one movie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Movie> findOne(String id) {
        log.debug("Request to get Movie : {}", id);
        return movieRepository.findById(id);
    }

    /**
     * Delete the movie by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Movie : {}", id);        movieRepository.deleteById(id);
    }
}
