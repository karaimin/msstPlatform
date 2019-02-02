package com.msst.platform.domain;

import java.util.Arrays;

public enum Language {
  ENGLISH("English"),
  BULGARIAN("Bulgarian"),
  SPANISH("Spanish"),
  ROMANIAN("Romanian");

  private final String value;

  Language(String value) {
    this.value = value;
  }

  public static Language fromString(String language) throws IllegalArgumentException {
    return Arrays.stream(Language.values())
        .filter(v -> v.value.equalsIgnoreCase(language))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown provider: ".concat(language)));
  }
}
