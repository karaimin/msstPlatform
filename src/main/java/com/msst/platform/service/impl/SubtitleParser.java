package com.msst.platform.service.impl;


import com.msst.platform.domain.Language;
import com.msst.platform.domain.Movie;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.domain.SubtitleLine;
import com.msst.platform.domain.file.locator.SubtitleContent;
import com.msst.platform.web.rest.errors.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class SubtitleParser {

  public static final String DEFAULT_VERSION = "00000000-0000-0000-0000-000000000000";

  private static final int LANGUAGE_INDEX = 0;
  private static final int MOVIE_NAME_INDEX = 1;
  private static final int VERSION_INDEX = 2;

  private static final String NAME_TOKENS_DELIMITER = "-";

  private SubtitleLineParser lineParser;

  public SubtitleParser() {
    this.lineParser = new SubtitleLineParser();
  }

  public Subtitle parse(SubtitleContent subtitleContent) {

    Subtitle subtitle = parseToSubtitle(subtitleContent.getProviderId(), subtitleContent.getSubtitleName());
    String fileContent = subtitleContent.getFileContent();

    if(fileContent != null){
      subtitle.setLines(getSubtitleLines(fileContent));
    }

    return subtitle;
  }

  private Subtitle parseToSubtitle(String providerId, String fileName){
    String[] nameTokens = fileName.split(NAME_TOKENS_DELIMITER);

    String movieName = nameTokens[MOVIE_NAME_INDEX];
    String language = nameTokens[LANGUAGE_INDEX];
    String version = nameTokens.length > VERSION_INDEX ? nameTokens[VERSION_INDEX] : DEFAULT_VERSION;

    Movie movie = new Movie();
    movie.setName(movieName);

    Subtitle subtitle = new Subtitle();
    subtitle.setMovie(movie);
    subtitle.setLanguage(Language.fromString(language));
    subtitle.setVersion(version);
    subtitle.setProviderId(providerId);

    return subtitle;
    // @formatter:off
  }

  private Set<SubtitleLine> getSubtitleLines(String content)  {
    Set<SubtitleLine> subtitleLines = new HashSet<>();
    String lineVersion = UUID.randomUUID().toString();

    try (BufferedReader fileContentReader = new BufferedReader(new StringReader(content))) {
      String line;
      List<String> lineChunks = new ArrayList<>();

      while ((line = fileContentReader.readLine()) != null) {
        String normalizedLine = line.trim();

        if (normalizedLine.isEmpty()) {
          subtitleLines.add(lineParser.parse(lineChunks, lineVersion));
          lineChunks.clear();
          continue;
        }

        lineChunks.add(normalizedLine);
      }

      if(!lineChunks.isEmpty()) {
        subtitleLines.add(lineParser.parse(lineChunks, lineVersion));
      }
      return subtitleLines;
    } catch (IOException ex){
      throw new InternalServerErrorException("Something went wrong wile parsing subtitle");
    }
  }
}
