package com.msst.platform.service.dto;

import com.msst.platform.domain.Language;

public class StartTranslateSubtitleTranslateInfo {
  private String providerId;
  private Language parentLanguage;
  private String parentVersion;
  private Language targetLanguage;

  protected StartTranslateSubtitleTranslateInfo() {

  }

  private StartTranslateSubtitleTranslateInfo(Builder builder) {
    this.providerId = builder.providerId;
    this.parentLanguage = builder.parentLanguage;
    this.parentVersion = builder.parentVersion;
    this.targetLanguage = builder.targetLanguage;
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getProviderId() {
    return providerId;
  }

  public Language getParentLanguage() {
    return parentLanguage;
  }

  public String getParentVersion() {
    return parentVersion;
  }

  public Language getTargetLanguage() {
    return targetLanguage;
  }

  public static class Builder {
    private String providerId;
    private Language parentLanguage;
    private String parentVersion;
    private Language targetLanguage;

    private Builder() {

    }

    private Builder(StartTranslateSubtitleTranslateInfo subtitleTranslateInfo) {
     this.providerId = subtitleTranslateInfo.providerId;
     this.parentLanguage = subtitleTranslateInfo.parentLanguage;
     this.parentVersion = subtitleTranslateInfo.parentVersion;
     this.targetLanguage = subtitleTranslateInfo.targetLanguage;
    }

    public Builder setProviderId(String providerId) {
      this.providerId = providerId;
      return this;
    }

    public Builder setParentLanguage(Language parentLanguage) {
      this.parentLanguage = parentLanguage;
      return this;
    }

    public Builder setParentVersion(String parentVersion) {
      this.parentVersion = parentVersion;
      return this;
    }

    public Builder setTargetLanguage(Language targetLanguage) {
      this.targetLanguage = targetLanguage;
      return this;
    }

    public StartTranslateSubtitleTranslateInfo build() {
      return new StartTranslateSubtitleTranslateInfo(this);
    }
  }
}
