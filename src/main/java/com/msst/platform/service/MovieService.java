package com.msst.platform.service;

import com.msst.platform.domain.Movie;
import com.msst.platform.domain.Subtitle;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing MovieInfo.
 */
public interface MovieService {

    /**
     * Save a movie.
     *
     * @param movie the entity to save
     * @return the persisted entity
     */
    Movie save(Movie movie);

    /**
     * Get all the movies.
     *
     * @return the list of entities
     */
    List<Movie> findAll();


    /**
     * Get the "id" movie.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Movie> findOne(String id);

    /**
     * Delete the "id" movie.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
