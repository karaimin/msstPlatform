package com.msst.platform.web.rest.errors;

import com.msst.platform.domain.Language;

public class SubtitleTranslationAlreadyStartedException extends BadRequestAlertException {

  public SubtitleTranslationAlreadyStartedException(String parentId, Language language) {
    super(String.format("Subtitle translation with language: '%s' and parent: '%s' already started", language, parentId), "Subtitle",
      "translationStarted");
  }
}
