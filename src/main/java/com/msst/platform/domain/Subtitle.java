package com.msst.platform.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Subtitle.
 */
@Document(collection = "subtitle")
public class Subtitle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("version")
    private String version;

    @DBRef
    @Field("movie")
    @JsonIgnoreProperties("subtitles")
    private Movie movie;

    @DBRef
    @Field("subtitle")
    @JsonIgnoreProperties("sources")
    private Subtitle subtitle;

    @DBRef
    @Field("source")
    private Set<Subtitle> sources = new HashSet<>();
    @DBRef
    @Field("lines")
    private Set<SubtitleLine> lines = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public Subtitle version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Movie getMovie() {
        return movie;
    }

    public Subtitle movie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Subtitle getSubtitle() {
        return subtitle;
    }

    public Subtitle subtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void setSubtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
    }

    public Set<Subtitle> getSources() {
        return sources;
    }

    public Subtitle sources(Set<Subtitle> subtitles) {
        this.sources = subtitles;
        return this;
    }

    public Subtitle addSource(Subtitle subtitle) {
        this.sources.add(subtitle);
        subtitle.setSubtitle(this);
        return this;
    }

    public Subtitle removeSource(Subtitle subtitle) {
        this.sources.remove(subtitle);
        subtitle.setSubtitle(null);
        return this;
    }

    public void setSources(Set<Subtitle> subtitles) {
        this.sources = subtitles;
    }

    public Set<SubtitleLine> getLines() {
        return lines;
    }

    public Subtitle lines(Set<SubtitleLine> subtitleLines) {
        this.lines = subtitleLines;
        return this;
    }

    public Subtitle addLines(SubtitleLine subtitleLine) {
        this.lines.add(subtitleLine);
        subtitleLine.setSubtitle(this);
        return this;
    }

    public Subtitle removeLines(SubtitleLine subtitleLine) {
        this.lines.remove(subtitleLine);
        subtitleLine.setSubtitle(null);
        return this;
    }

    public void setLines(Set<SubtitleLine> subtitleLines) {
        this.lines = subtitleLines;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subtitle subtitle = (Subtitle) o;
        if (subtitle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subtitle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Subtitle{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
