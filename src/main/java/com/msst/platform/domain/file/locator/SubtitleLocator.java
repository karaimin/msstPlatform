package com.msst.platform.domain.file.locator;


import com.msst.platform.domain.Subtitle;

import java.io.File;
import java.util.List;

public interface SubtitleLocator {
  String FILE_NAME_DELIMITER = "-";

  Subtitle locate(String fileName);

  List<SubtitleContent> getAllSubtitles();

  List<SubtitleContent> getSubtitlesByMovieName(String movieName);

  SubtitleContent getSubtitleInfoById(String id);

  SubtitleContent downloadSubtitleFile(String id);

  boolean uploadFile(File file, String fileName);
}
