package com.msst.platform.web.rest;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.service.LineVersionAddedEvent;
import com.msst.platform.service.LineVersionAddedEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/sse")
public class SubtitleResourceSSE {

  private final Flux<LineVersionAddedEvent> events;

  public SubtitleResourceSSE(LineVersionAddedEventPublisher versionPublisher) {
    this.events = Flux.create(versionPublisher).share();
  }


  @GetMapping(value = "/subtitles/lines/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<LineVersion> getTranslatedLineVersionsReactive(@PathVariable String id) {
    return this.events
      .filter(versionAddedEvent -> id.equals(versionAddedEvent.getSubtitleId()))
      .map(LineVersionAddedEvent::getLineVersion);
  }
}
