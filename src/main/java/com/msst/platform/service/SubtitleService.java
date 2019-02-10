package com.msst.platform.service;

import com.msst.platform.domain.Subtitle;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Subtitle.
 */
public interface SubtitleService {

    /**
     * Save a subtitle.
     *
     * @param subtitle the entity to create
     * @return the persisted entity
     */
    Subtitle create(Subtitle subtitle);

    Subtitle update(Subtitle subtitle);

    /**
     * Get all the subtitles.
     *
     * @return the list of entities
     */
    List<Subtitle> findAll();


    /**
     * Get the "id" subtitle.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Subtitle> findOne(String id);

    /**
     * Delete the "id" subtitle.
     *
     * @param id the id of the entity
     */
    void delete(String id);

   List<Subtitle> getTranslatedSubtitles();

   List<Subtitle> getTranslatedSubtitles(String movieName);

   Subtitle getTranslatedSubtitle(String providerId);

   Subtitle downloadSubtitle(String providerId);

   Subtitle getTranslationSourceSubtitle(StartTranslateSubtitleTranslateInfo subtitleInfo, String movieId);

   Subtitle getParentSubtitle(String subtitleId);
}
