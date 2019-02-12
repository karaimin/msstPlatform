package com.msst.platform.service;

import com.msst.platform.domain.LineVersion;
import org.springframework.context.ApplicationEvent;

public class LineVersionAddedEvent extends ApplicationEvent {
  private final String subtitleId;
  private final LineVersion lineVersion;

  public LineVersionAddedEvent(LineVersion source, String subtitleId) {
    super(source);
    this.subtitleId = subtitleId;
    this.lineVersion = source;
  }

  public String getSubtitleId() {
    return subtitleId;
  }

  public LineVersion getLineVersion() {
    return lineVersion;
  }
}
