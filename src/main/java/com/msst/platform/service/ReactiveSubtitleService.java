package com.msst.platform.service;

import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.SubtitleLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveSubtitleService {
    Mono<Subtitle> getMovieSubtitle(String subtitleId);

    Mono<Subtitle> getPendingSubtitle(String subtitleId);

    Flux<SubtitleLine> getSubtitleLines(String subtitleId);

    Flux<SubtitleLine> getParentSubtitleLines(String subtitleId);
}
