package com.msst.platform.service;

import com.msst.platform.domain.LineVersionRating;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing LineVersionRating.
 */
public interface LineVersionRatingService {

    /**
     * Save a lineVersionRating.
     *
     * @param lineVersionRating the entity to save
     * @return the persisted entity
     */
    LineVersionRating save(LineVersionRating lineVersionRating);

    /**
     * Get all the lineVersionRatings.
     *
     * @return the list of entities
     */
    List<LineVersionRating> findAll();


    /**
     * Get the "id" lineVersionRating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LineVersionRating> findOne(String id);

    /**
     * Delete the "id" lineVersionRating.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
