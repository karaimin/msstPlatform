package com.msst.platform.service.impl;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.domain.SubtitleLine;
import org.mapstruct.ap.internal.util.Collections;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

public class SubtitleLineParser {
    private static final int LINENUMBER_INDEX = 0;
    private static final int TIME_INDEX = 1;
    private static final int TEXT_INDEX = 2;

    private static final String TIME_DELIMITER = ":";
    private static final String DURATION_DELIMITER = "-->";
    private static final String DURATION_START_PREFIX = "PT";

    private static final String HOURS_PATTERN = "%sH";
    private static final String MINUTES_PATTERN = "%sM";
    private static final String SECONDS_PATTERN = "%sS";

    private static final char SECONDS_SUBTITLE_FLOATING_POINT_DELIMITER = ',';
    private static final char SECONDS_JAVA_FLOATING_POINT_DELIMITER = '.';

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public SubtitleLine parse(List<String> orderedLineChunk) {
        SubtitleLine subtitleLine = new SubtitleLine();

        parseLineNumber(orderedLineChunk, subtitleLine);
        parseTime(orderedLineChunk, subtitleLine);
        parseText(orderedLineChunk, subtitleLine);

        return subtitleLine;
    }

    private void parseLineNumber(List<String> orderedLineChunk, SubtitleLine subtitleLineBuilder) {
        subtitleLineBuilder.setSequenceNumber(Integer.parseInt(orderedLineChunk.get(LINENUMBER_INDEX)));
    }

    private void parseTime(List<String> orderedLineChunk, SubtitleLine subtitleLineBuilder) {
        String[] durationTokens = orderedLineChunk.get(TIME_INDEX).split(DURATION_DELIMITER);

        String startTimeToken = durationTokens[0].trim();
        String endTimeToken = durationTokens[1].trim();

        subtitleLineBuilder.setStartTime(parseDuration(startTimeToken));
        subtitleLineBuilder.setEndTime(parseDuration(endTimeToken));
    }

    private Duration parseDuration(String subtitleDuration) {
        String[] timeTokens = subtitleDuration.split(TIME_DELIMITER);

        String hours = timeTokens[0];
        String minutes = timeTokens[1];
        String seconds = timeTokens[2].replace(SECONDS_SUBTITLE_FLOATING_POINT_DELIMITER, SECONDS_JAVA_FLOATING_POINT_DELIMITER);

        return Duration.parse(createDurationParseString(hours, minutes, seconds));
    }

    private String createDurationParseString(String hours, String minutes, String seconds) {
        String hoursParseToken = String.format(HOURS_PATTERN, hours);
        String minutesParseToken = String.format(MINUTES_PATTERN, minutes);
        String secondsParseToken = String.format(SECONDS_PATTERN, seconds);

        // @formatter:off
    return new StringBuilder(DURATION_START_PREFIX)
        .append(hoursParseToken)
        .append(minutesParseToken)
        .append(secondsParseToken)
        .toString();
    // @formatter:on
    }

    private void parseText(List<String> orderedLineChunk, SubtitleLine subtitleLineBuilder) {
        List<String> textChunks = orderedLineChunk.subList(TEXT_INDEX, orderedLineChunk.size());

        String text = String.join(LINE_SEPARATOR, textChunks);
        LineVersion lineVersion = new LineVersion();
        lineVersion.setText(text);

        subtitleLineBuilder.setVersions(new HashSet<>(Collections.asSet(lineVersion)));
    }
}
