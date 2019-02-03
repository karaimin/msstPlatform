package com.msst.platform.domain.file.locator;

import java.util.Arrays;

public enum FileLocatorProviderType {
  GOOGLE("Google");

  private final String value;

  FileLocatorProviderType(String value) {
    this.value = value;
  }

  public static FileLocatorProviderType fromString(String providerName) throws IllegalArgumentException {
    return Arrays.stream(FileLocatorProviderType.values())
        .filter(v -> v.value.equalsIgnoreCase(providerName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown provider: ".concat(providerName)));
  }
}
