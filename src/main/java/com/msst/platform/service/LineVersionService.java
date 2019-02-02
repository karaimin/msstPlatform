package com.msst.platform.service;

import com.msst.platform.domain.LineVersion;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing LineVersion.
 */
public interface LineVersionService {

    /**
     * Save a lineVersion.
     *
     * @param lineVersion the entity to save
     * @return the persisted entity
     */
    LineVersion save(LineVersion lineVersion);

    /**
     * Get all the lineVersions.
     *
     * @return the list of entities
     */
    List<LineVersion> findAll();


    /**
     * Get the "id" lineVersion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LineVersion> findOne(String id);

    /**
     * Delete the "id" lineVersion.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
