package com.msst.platform.service.impl;

import com.msst.platform.service.SubtitleLineService;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.repository.SubtitleLineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing SubtitleLine.
 */
@Service
public class SubtitleLineServiceImpl implements SubtitleLineService {

    private final Logger log = LoggerFactory.getLogger(SubtitleLineServiceImpl.class);

    private final SubtitleLineRepository subtitleLineRepository;

    public SubtitleLineServiceImpl(SubtitleLineRepository subtitleLineRepository) {
        this.subtitleLineRepository = subtitleLineRepository;
    }

    /**
     * Save a subtitleLine.
     *
     * @param subtitleLine the entity to save
     * @return the persisted entity
     */
    @Override
    public SubtitleLine save(SubtitleLine subtitleLine) {
        log.debug("Request to save SubtitleLine : {}", subtitleLine);
        return subtitleLineRepository.save(subtitleLine);
    }

    /**
     * Get all the subtitleLines.
     *
     * @return the list of entities
     */
    @Override
    public List<SubtitleLine> findAll() {
        log.debug("Request to get all SubtitleLines");
        return subtitleLineRepository.findAll();
    }


    /**
     * Get one subtitleLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<SubtitleLine> findOne(String id) {
        log.debug("Request to get SubtitleLine : {}", id);
        return subtitleLineRepository.findById(id);
    }

    /**
     * Delete the subtitleLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SubtitleLine : {}", id);        subtitleLineRepository.deleteById(id);
    }
}
