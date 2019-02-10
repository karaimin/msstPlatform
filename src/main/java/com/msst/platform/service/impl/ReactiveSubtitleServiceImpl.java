package com.msst.platform.service.impl;

import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.repository.reactive.ReactiveSubtitleLineRepository;
import com.msst.platform.repository.reactive.ReactiveSubtitleRepository;
import com.msst.platform.service.ReactiveSubtitleService;
import com.msst.platform.web.rest.errors.SubtitleFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class ReactiveSubtitleServiceImpl implements ReactiveSubtitleService {
  @Autowired
  private ReactiveSubtitleRepository reactiveSubtitleRepository;

  @Autowired
  private ReactiveSubtitleLineRepository reactiveLinesRepository;

  @Override
  public Mono<Subtitle> getMovieSubtitle(String subtitleId) {
    return reactiveSubtitleRepository.findById(subtitleId);
  }

  @Override
  public Mono<Subtitle> getPendingSubtitle(String subtitleId) {
    return reactiveSubtitleRepository.findById(subtitleId).filter(subtitle -> subtitle.getParent() != null)
      .switchIfEmpty(Mono.error(new SubtitleFormatException(String.format("There is no subtitle with id '%s' that can be translated", subtitleId))))
      .map(subtitle -> {
        sortSubtitleLines(subtitle.getParent());
        sortSubtitleLines(subtitle);
        return subtitle;
      });
  }

  @Override
  public Flux<SubtitleLine> getSubtitleLines(String subtitleId) {
    return reactiveLinesRepository.findAllBySubtitleId(subtitleId);
  }

  @Override
  public Flux<SubtitleLine> getParentSubtitleLines(String subtitleId) {
     return reactiveSubtitleRepository.findById(subtitleId)
      .map(subtitle -> subtitle.getParent().getLines())
      .flatMapMany(Flux::fromIterable)
       .sort(Comparator.comparing(SubtitleLine::getSequenceNumber));

  }

  private void sortSubtitleLines(Subtitle subtitle) {
    if (subtitle == null) {
      return;
    }

    Set<SubtitleLine> subtitleLines = new TreeSet<>(Comparator.comparing(SubtitleLine::getSequenceNumber));
    subtitleLines.addAll(subtitle.getLines());
    subtitle.setLines(subtitleLines);
  }
}
