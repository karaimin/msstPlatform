package com.msst.platform.facade.impl;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.domain.Movie;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.facade.MsstPlatformFacade;
import com.msst.platform.service.*;
import com.msst.platform.service.dto.LineVersionInfo;
import com.msst.platform.service.dto.MovieInfo;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;
import com.msst.platform.service.dto.TranslatingLineInfo;
import com.msst.platform.service.mapper.MovieMapper;
import com.msst.platform.web.rest.errors.InternalServerErrorException;
import com.msst.platform.web.rest.errors.MovieNotFoundException;
import com.msst.platform.web.rest.errors.SubtitleFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultMsstPlaftormFacade implements MsstPlatformFacade {

  @Autowired
  private MovieService movieService;

  @Autowired
  private SubtitleService subtitleService;

  @Autowired
  private ReactiveSubtitleService reactiveSubtitleService;

  @Autowired
  private SubtitleLineService subtitleLineService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Override
  public Movie createMovie(Movie movie) {
    return movieService.save(movie);
  }

  @Override
  public List<MovieInfo> getAllMovieInfos() {
    // @formatter:off
        return movieService.findAll().stream()
            .map(this::getMovieInfo)
            .collect(Collectors.toList());
        // @formatter:on
  }

  @Override
  public Optional<MovieInfo> getMovieInfo(String id) {
    return movieService.findOne(id).map(this::getMovieInfo);
  }

  @Override
  public void delete(String id) {
    movieService.delete(id);
  }

  private MovieInfo getMovieInfo(Movie movie) {
    // @formatter:off
        return MovieMapper.convertToMovieInfo(movie)
            .setPendingTranslates(
                movie.getSubtitles().stream()
                    .filter(subtitle -> subtitle.getChildren().isEmpty())
                    .collect(Collectors.toSet())
            )
            .setTranslatedSubtitles(subtitleService.getTranslatedSubtitles(movie.getName()))
            .setPendingTranslates(movie.getSubtitles().stream().filter(subtitle -> subtitle.getParent() != null).collect(Collectors.toList()))
            .build();
        // @formatter:on
  }

  @Override
  public void startTranslateSubtitle(StartTranslateSubtitleTranslateInfo subtitleTranslateInfo, String movieId) {
    Movie movie = movieService.findOne(movieId)
      .orElseThrow(() -> new MovieNotFoundException(String.format("Movie with id: '%s' does not found", movieId)));

    Subtitle parentSubtitle = subtitleService.getTranslationSourceSubtitle(subtitleTranslateInfo, movieId);
    parentSubtitle.setLines(new HashSet<>(subtitleLineService.creatBulkWithSameVersion(parentSubtitle.getLines())));
    parentSubtitle.setMovie(movie);

    // @formatter:off
    Subtitle createdSubtitle = subtitleService.create(
      new Subtitle()
        .version(UUID.randomUUID().toString())
        .language(subtitleTranslateInfo.getTargetLanguage())
        .movie(movie)
        .lines(new HashSet<>(subtitleLineService.createLinesWithEmptyVersions(parentSubtitle.getLines())))
        .parent(subtitleService.create(parentSubtitle))
    );
    // @formatter:on
    movie.addSubtitles(createdSubtitle);
    movieService.save(movie);
  }

  @Override
  public Mono<Subtitle> getPendingSubtitle(String id)  {
    return reactiveSubtitleService.getPendingSubtitle(id);
  }

  @Override
  public List<LineVersion> getTranslatedLineVersions(String lineId) {
    return new ArrayList<>(
      subtitleLineService
        .findOne(lineId)
        .orElseThrow(() -> new SubtitleFormatException("Subtitle line is not found"))
        .getVersions()
    );
  }

  public Flux<TranslatingLineInfo> getParentLinesInfo(String subtitleId) {
    return reactiveSubtitleService.getParentSubtitleLines(subtitleId)
      .map(subtitleLine -> {
        TranslatingLineInfo lineInfo = new TranslatingLineInfo();
        lineInfo.setSequence(subtitleLine.getSequenceNumber());
        lineInfo.setParentLineId(subtitleLine.getId());
        lineInfo.setParentText(
          subtitleLine.getVersions().stream()
            .findFirst()
            .orElseThrow(() -> new InternalServerErrorException("No versions found in parent subtitle"))
            .getText()
        );
        return lineInfo;
      });

  }

  @Override
  public List<TranslatingLineInfo> getParentLinesInfoList(String subtitleId) {
    Map<Integer, String> subtitleLines = new HashMap<>();
    subtitleService
      .findOne(subtitleId)
      .orElseThrow(() -> new SubtitleFormatException("Subtitle was not found"))
      .getLines()
      .forEach(line -> subtitleLines.put(line.getSequenceNumber(), line.getId()));

    return subtitleService.getParentSubtitle(subtitleId).getLines().stream()
      .sorted(Comparator.comparing(SubtitleLine::getSequenceNumber))
      .map(subtitleLine -> {
        TranslatingLineInfo lineInfo = new TranslatingLineInfo();
        lineInfo.setSequence(subtitleLine.getSequenceNumber());
        lineInfo.setParentLineId(subtitleLine.getId());
        lineInfo.setCurrentLineId(subtitleLines.get(subtitleLine.getSequenceNumber()));
        lineInfo.setParentText(
          subtitleLine.getVersions().stream()
            .findFirst()
            .orElseThrow(() -> new InternalServerErrorException("No versions found in parent subtitle"))
            .getText()
        );
        return lineInfo;
      })
      .collect(Collectors.toList());
  }

  @Override
  public LineVersion addNewTranslatedLine(String id, LineVersion lineVersion) {
    LineVersion newLineVersion = subtitleLineService.addNewTranslatedLine(id, lineVersion);

    this.eventPublisher.publishEvent(new LineVersionAddedEvent(newLineVersion, id));
    return newLineVersion;
  }

  @Override
  public void finishSubtitleTranslate(String subtitleId) {
    Subtitle subtitle = subtitleService.finishSubtitleTranslate(subtitleId);

    Movie movie = subtitle.getMovie();
    movie.removeSubtitles(subtitle);

    movieService.save(movie);
  }

  @Override
  public List<Subtitle> getSubtitlesFinishedTranslation() {
    return subtitleService.getSubtitlesFinishedTranslation();
  }
}
