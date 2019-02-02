package com.msst.platform.service.impl;

import com.msst.platform.service.LineVersionService;
import com.msst.platform.domain.LineVersion;
import com.msst.platform.repository.LineVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing LineVersion.
 */
@Service
public class LineVersionServiceImpl implements LineVersionService {

    private final Logger log = LoggerFactory.getLogger(LineVersionServiceImpl.class);

    private final LineVersionRepository lineVersionRepository;

    public LineVersionServiceImpl(LineVersionRepository lineVersionRepository) {
        this.lineVersionRepository = lineVersionRepository;
    }

    /**
     * Save a lineVersion.
     *
     * @param lineVersion the entity to save
     * @return the persisted entity
     */
    @Override
    public LineVersion save(LineVersion lineVersion) {
        log.debug("Request to save LineVersion : {}", lineVersion);
        return lineVersionRepository.save(lineVersion);
    }

    /**
     * Get all the lineVersions.
     *
     * @return the list of entities
     */
    @Override
    public List<LineVersion> findAll() {
        log.debug("Request to get all LineVersions");
        return lineVersionRepository.findAll();
    }


    /**
     * Get one lineVersion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<LineVersion> findOne(String id) {
        log.debug("Request to get LineVersion : {}", id);
        return lineVersionRepository.findById(id);
    }

    /**
     * Delete the lineVersion by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete LineVersion : {}", id);        lineVersionRepository.deleteById(id);
    }
}
