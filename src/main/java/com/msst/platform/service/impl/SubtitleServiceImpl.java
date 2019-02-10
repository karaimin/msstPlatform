package com.msst.platform.service.impl;

import com.msst.platform.domain.Language;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.file.locator.SubtitleContent;
import com.msst.platform.domain.file.locator.SubtitleLocator;
import com.msst.platform.repository.SubtitleRepository;
import com.msst.platform.service.SubtitleService;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;
import com.msst.platform.web.rest.errors.SubtitleFormatException;
import com.msst.platform.web.rest.errors.SubtitleTranslationAlreadyStartedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Subtitle.
 */
@Service
public class SubtitleServiceImpl implements SubtitleService {

  private final Logger log = LoggerFactory.getLogger(SubtitleServiceImpl.class);

  private final SubtitleRepository subtitleRepository;

  private SubtitleLocator fileLocator;

  public SubtitleServiceImpl(
    @Autowired SubtitleRepository subtitleRepository,
    @Autowired @Qualifier("fileLocatorAlias") SubtitleLocator subtitleLocator
  ) {
    this.subtitleRepository = subtitleRepository;
    this.fileLocator = subtitleLocator;
  }

  /**
   * Save a subtitle.
   *
   * @param subtitle the entity to create
   * @return the persisted entity
   */
  @Override
  public Subtitle create(Subtitle subtitle) {
    log.debug("Request to create Subtitle : {}", subtitle);
    return subtitleRepository.save(subtitle);
  }

  @Override
  public Subtitle update(Subtitle subtitle) {
    return null;
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
    log.debug("Request to delete Subtitle : {}", id);
    subtitleRepository.deleteById(id);
  }

  @Override
  public List<Subtitle> getTranslatedSubtitles() {
    List<SubtitleContent> subtitleContents = fileLocator.getAllSubtitles();
    return convertSubtitleContent(subtitleContents);
  }

  @Override
  public List<Subtitle> getTranslatedSubtitles(String movieName) {
    List<SubtitleContent> subtitleContents = fileLocator.getSubtitlesByMovieName(movieName);
    return convertSubtitleContent(subtitleContents);
  }

  @Override
  public Subtitle getTranslatedSubtitle(String providerId) {
    SubtitleContent subtitleContent = fileLocator.getSubtitleInfoById(providerId);
    return convertSubtitleContent(subtitleContent);
  }

  @Override
  public Subtitle downloadSubtitle(String providerId) {
    SubtitleContent subtitleContent = fileLocator.downloadSubtitleFile(providerId);
    return convertSubtitleContent(subtitleContent);
  }

  private Optional<Subtitle> getLocalTranslatedSubtitle(String movieId, String version, Language language) {
    return subtitleRepository.findByVersionAndMovieIdAndLanguage(version, movieId, language).stream()
      .filter(subtitle -> subtitle.getParent() == null)
      .findFirst();
  }

  private Optional<Subtitle> getLocalSubtitlePendingTranslaion(Subtitle parent, Language language) {
    return subtitleRepository.findByParentAndLanguage(parent, language);
  }

  @Override
  public Subtitle getTranslationSourceSubtitle(StartTranslateSubtitleTranslateInfo subtitleInfo, String movieId) {
    Language parentLanguage = subtitleInfo.getParentLanguage();
    Optional<Subtitle> parentLocalSubtitle = getLocalTranslatedSubtitle(movieId, subtitleInfo.getParentVersion(), parentLanguage);

    if(parentLocalSubtitle.isPresent()
      && getLocalSubtitlePendingTranslaion(parentLocalSubtitle.get(), subtitleInfo.getTargetLanguage()).isPresent()
    ) {
      throw new SubtitleTranslationAlreadyStartedException(parentLocalSubtitle.get().getId(), parentLanguage);
    }

    return parentLocalSubtitle.orElseGet(() -> downloadSubtitle(subtitleInfo.getProviderId()));
  }

  @Override
  public Subtitle getParentSubtitle(String subtitleId) {
    return subtitleRepository.findById(subtitleId).orElseThrow(() -> new SubtitleFormatException("some")).getParent();
  }

  private Subtitle convertSubtitleContent(SubtitleContent content) {
    SubtitleParser subtitleParser = new SubtitleParser();
    return subtitleParser.parse(content);
  }

  private List<Subtitle> convertSubtitleContent(Collection<SubtitleContent> contents) {
    // @formatter:off
    return contents.stream()
        .map(this::convertSubtitleContent)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
