package com.msst.platform.service.impl;

import com.msst.platform.service.LineVersionRatingService;
import com.msst.platform.domain.LineVersionRating;
import com.msst.platform.repository.LineVersionRatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing LineVersionRating.
 */
@Service
public class LineVersionRatingServiceImpl implements LineVersionRatingService {

    private final Logger log = LoggerFactory.getLogger(LineVersionRatingServiceImpl.class);

    private final LineVersionRatingRepository lineVersionRatingRepository;

    public LineVersionRatingServiceImpl(LineVersionRatingRepository lineVersionRatingRepository) {
        this.lineVersionRatingRepository = lineVersionRatingRepository;
    }

    /**
     * Save a lineVersionRating.
     *
     * @param lineVersionRating the entity to save
     * @return the persisted entity
     */
    @Override
    public LineVersionRating save(LineVersionRating lineVersionRating) {
        log.debug("Request to save LineVersionRating : {}", lineVersionRating);
        return lineVersionRatingRepository.save(lineVersionRating);
    }

    /**
     * Get all the lineVersionRatings.
     *
     * @return the list of entities
     */
    @Override
    public List<LineVersionRating> findAll() {
        log.debug("Request to get all LineVersionRatings");
        return lineVersionRatingRepository.findAll();
    }


    /**
     * Get one lineVersionRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<LineVersionRating> findOne(String id) {
        log.debug("Request to get LineVersionRating : {}", id);
        return lineVersionRatingRepository.findById(id);
    }

    /**
     * Delete the lineVersionRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete LineVersionRating : {}", id);        lineVersionRatingRepository.deleteById(id);
    }
}
