package com.msst.platform.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
    @Field("parent")
    @JsonIgnoreProperties("children")
    private Subtitle parent;

    @DBRef
    @Field("children")
    private Set<Subtitle> children = new HashSet<>();

    @DBRef
    @Field("lines")
    private Set<SubtitleLine> lines = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Field("language")
    private Language language;

    @Transient
    private String providerId;

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

    public Subtitle getParent() {
        return parent;
    }

    public Subtitle parent(Subtitle subtitle) {
        this.parent = subtitle;
        return this;
    }

    public void setParent(Subtitle parent) {
        this.parent = parent;
    }

    public Set<Subtitle> getChildren() {
        return children;
    }

    public Subtitle sources(Set<Subtitle> subtitles) {
        this.children = subtitles;
        return this;
    }

    public Subtitle addSource(Subtitle subtitle) {
        this.children.add(subtitle);
        subtitle.setParent(this);
        return this;
    }

    public Subtitle removeSource(Subtitle subtitle) {
        this.children.remove(subtitle);
        subtitle.setParent(null);
        return this;
    }

    public void setChildren(Set<Subtitle> subtitles) {
        this.children = subtitles;
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

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Subtitle language(Language language) {
      this.language = language;
      return this;
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
