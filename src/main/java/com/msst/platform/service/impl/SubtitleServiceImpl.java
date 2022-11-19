package com.msst.platform.service.impl;

import com.msst.platform.domain.Language;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.domain.file.locator.SubtitleContent;
import com.msst.platform.domain.file.locator.SubtitleLocator;
import com.msst.platform.repository.SubtitleRepository;
import com.msst.platform.service.SubtitleService;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;
import com.msst.platform.web.rest.errors.FinishTranslationException;
import com.msst.platform.web.rest.errors.InternalServerErrorException;
import com.msst.platform.web.rest.errors.SubtitleFormatException;
import com.msst.platform.web.rest.errors.SubtitleTranslationAlreadyStartedException;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
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

  @Override
  public List<Subtitle> getSubtitlesFinishedTranslation() {
    return subtitleRepository.findAllByParentNotNull().stream()
      .filter(subtitle -> subtitle.getLines().stream().noneMatch(subtitleLine -> subtitleLine.getVersions().isEmpty()))
      .peek(subtitle -> subtitle.setLines(Collections.emptySet()))
      .collect(Collectors.toList());
  }

  @Override
  public Subtitle finishSubtitleTranslate(String subtitleId) {
    Subtitle subtitle = subtitleRepository.findById(subtitleId)
      .orElseThrow(() -> new SubtitleFormatException("Subtitle not found"));

    validateSubtitleIsTranslated(subtitle);
    String fileName = String.format("%s-%s-%s", subtitle.getLanguage(), subtitle.getMovie().getName(), subtitle.getVersion());
    File localFile = writeSubtitlesToTempFile(convertToWritableLines(subtitle));

    boolean uploadResult = fileLocator.uploadFile(localFile, fileName);
    if(!uploadResult) {
      throw new FinishTranslationException("Error while importing file to remote drive service");
    }

    subtitleRepository.delete(subtitle);
    return subtitle;
  }

  private void validateSubtitleIsTranslated(Subtitle subtitle) {
    if(subtitle.getLines().stream().map(SubtitleLine::getVersions).anyMatch(Set::isEmpty)){
      throw new SubtitleFormatException("Subtitle is not fully translated");
    }
  }

  private File writeSubtitlesToTempFile(Collection<WritableLine> writableLines) {

    try {
      File tempFile = Files.createTempFile("subtitle-", ".srt").toFile();
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
        for(WritableLine line: writableLines) {
          writer.write(String.valueOf(line.getSequence()));
          writer.newLine();

          writer.write(line.getDuration());
          writer.newLine();

          writer.write(line.getText());
          writer.newLine();
          writer.newLine();
        }
      }

      return tempFile;
    } catch (IOException e) {
      throw new InternalServerErrorException("Something went wrong while saving file");
    }
  }

  private Collection<WritableLine> convertToWritableLines(Subtitle subtitle) {
    Collection<WritableLine> result = new ArrayList<>();

    for(SubtitleLine subtitleLine : subtitle.getLines()) {
      WritableLine writableLine = new WritableLine();
      writableLine.setSequence(subtitleLine.getSequenceNumber());

      String startTimeDuration = DurationFormatUtils.formatDuration(subtitleLine.getStartTime().toMillis(), "HH:mm:ss");
      String endTimeDuration = DurationFormatUtils.formatDuration(subtitleLine.getEndTime().toMillis(), "HH:mm:ss");

      String duration = String.format("%s --> %s", startTimeDuration, endTimeDuration);
      writableLine.setDuration(duration);
      writableLine.setText(subtitleLine.getVersions().stream()
        .findAny()
        .orElseThrow(() -> new InternalServerErrorException("Translated line does not contains any versions"))
        .getText()
      );

      result.add(writableLine);
    }
    return result;
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
