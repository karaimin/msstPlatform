package com.msst.platform.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msst.platform.domain.Movie;
import com.msst.platform.domain.Subtitle;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class MovieInfo {
    private String id;
    private String name;
    private Duration duration;
    private String description;

    private MovieInfo(Builder builder) {
        this.id = builder.resourceId;
        this.name = builder.name;
        this.duration = builder.duration;
        this.description = builder.description;
        this.translatedSubtitles = builder.translatedSubtitles;
        this.pendingTranslates = builder.pendingTranslates;
    }

    @JsonIgnoreProperties("movie")
    private Collection<Subtitle> translatedSubtitles;

    @JsonIgnoreProperties("movie")
    private Collection<Subtitle> pendingTranslates;

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Subtitle> getTranslatedSubtitles() {
        return translatedSubtitles;
    }

    public Collection<Subtitle> getPendingTranslates() {
        return pendingTranslates;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private String resourceId;
        private String name;
        private Duration duration;
        private String description;
        private Collection<Subtitle> translatedSubtitles;
        private Collection<Subtitle> pendingTranslates;

        private Builder() {

        }

        private Builder(MovieInfo movieInfo) {
            this.resourceId = movieInfo.id;
            this.name = movieInfo.name;
            this.duration = movieInfo.duration;
            this.description = movieInfo.description;
            this.translatedSubtitles = movieInfo.translatedSubtitles;
            this.pendingTranslates = movieInfo.pendingTranslates;
        }
        public Builder setResourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTranslatedSubtitles(Collection<Subtitle> translatedSubtitles) {
            this.translatedSubtitles = translatedSubtitles;
            return this;
        }

        public Builder setPendingTranslates(Collection<Subtitle> pendingTranslates) {
            this.pendingTranslates = pendingTranslates;
            return this;
        }

        public MovieInfo build() {
            return new MovieInfo(this);
        }
    }

  public static void main(String[] args) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    MovieInfo movieInfo = MovieInfo.builder().setDuration(Duration.of(5, ChronoUnit.SECONDS)).build();
    System.out.println(objectMapper.writeValueAsString(movieInfo));
  }
}
