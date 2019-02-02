package com.msst.platform.domain.file.locator;

public class SubtitleContent {
  private String subtitleName;
  private String fileContent;
  private String providerId;

  private SubtitleContent(Builder builder) {
    this.subtitleName = builder.subtitleName;
    this.fileContent = builder.fileContent;
    this.providerId = builder.providerId;
  }

  public String getSubtitleName() {
    return subtitleName;
  }

  public String getFileContent() {
    return fileContent;
  }

  public String getProviderId() {
    return providerId;
  }

  public static class Builder{
    private String subtitleName;
    private String fileContent;
    private String providerId;

    public Builder setSubtitleName(String subtitleName) {
      this.subtitleName = subtitleName;
      return this;
    }

    public Builder setFileContent(String fileContent) {
      this.fileContent = fileContent;
      return this;
    }

    public Builder setProviderId(String providerId) {
      this.providerId = providerId;
      return this;
    }

    public SubtitleContent build(){
      return new SubtitleContent(this);
    }

  }
}
