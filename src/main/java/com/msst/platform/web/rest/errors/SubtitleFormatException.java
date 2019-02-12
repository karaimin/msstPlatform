package com.msst.platform.web.rest.errors;

public class SubtitleFormatException extends BadRequestAlertException {
  public SubtitleFormatException(String defaultMessage) {
    super(defaultMessage, "Subtitle", "subtitleNotFound");
  }
}
