package com.msst.platform.service.impl;

import com.msst.platform.service.SubtitleService;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.repository.SubtitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Subtitle.
 */
@Service
public class SubtitleServiceImpl implements SubtitleService {

    private final Logger log = LoggerFactory.getLogger(SubtitleServiceImpl.class);

    private final SubtitleRepository subtitleRepository;

    public SubtitleServiceImpl(SubtitleRepository subtitleRepository) {
        this.subtitleRepository = subtitleRepository;
    }

    /**
     * Save a subtitle.
     *
     * @param subtitle the entity to save
     * @return the persisted entity
     */
    @Override
    public Subtitle save(Subtitle subtitle) {
        log.debug("Request to save Subtitle : {}", subtitle);
        return subtitleRepository.save(subtitle);
    }

    /**
     * Get all the subtitles.
     *
     * @return the list of entities
     */
    @Override
    public List<Subtitle> findAll() {
        log.debug("Request to get all Subtitles");
        return subtitleRepository.findAll();
    }


    /**
     * Get one subtitle by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Subtitle> findOne(String id) {
        log.debug("Request to get Subtitle : {}", id);
        return subtitleRepository.findById(id);
    }

    /**
     * Delete the subtitle by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Subtitle : {}", id);        subtitleRepository.deleteById(id);
    }
}
