package com.msst.platform.service;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.domain.SubtitleLine;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SubtitleLine.
 */
public interface SubtitleLineService {

  /**
   * Save a subtitleLine.
   *
   * @param subtitleLine the entity to save
   * @return the persisted entity
   */
  SubtitleLine save(SubtitleLine subtitleLine);

  /**
   * Get all the subtitleLines.
   *
   * @return the list of entities
   */
  List<SubtitleLine> findAll();

  /**
   * Get the "id" subtitleLine.
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<SubtitleLine> findOne(String id);

  /**
   * Delete the "id" subtitleLine.
   *
   * @param id the id of the entity
   */
  void delete(String id);

  Collection<SubtitleLine> creatBulkWithSameVersion(Collection<SubtitleLine> subtitleLines);

  Collection<SubtitleLine> createLinesWithEmptyVersions(Collection<SubtitleLine> subtitleLines);

  LineVersion addNewTranslatedLine(String subtitleId, LineVersion lineVersion);
}
