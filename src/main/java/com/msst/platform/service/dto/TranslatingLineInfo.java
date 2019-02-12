package com.msst.platform.service.dto;

public class TranslatingLineInfo {
  private int sequence;
  private String parentLineId;
  private String currentLineId;
  private String parentText;

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public String getParentLineId() {
    return parentLineId;
  }

  public void setParentLineId(String parentLineId) {
    this.parentLineId = parentLineId;
  }

  public String getParentText() {
    return parentText;
  }

  public void setParentText(String parentText) {
    this.parentText = parentText;
  }

  public String getCurrentLineId() {
    return currentLineId;
  }

  public void setCurrentLineId(String currentLineId) {
    this.currentLineId = currentLineId;
  }
}
