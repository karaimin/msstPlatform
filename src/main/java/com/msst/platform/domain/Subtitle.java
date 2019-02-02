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
    private Set<Movie> movies = new HashSet<>();
    @DBRef
    @Field("subtitle")
    @JsonIgnoreProperties("sources")
    private Subtitle subtitle;

    @DBRef
    @Field("source")
    private Set<Subtitle> sources = new HashSet<>();
    @DBRef
    @Field("subtitleLine")
    @JsonIgnoreProperties("subtitles")
    private SubtitleLine subtitleLine;

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

    public Set<Movie> getMovies() {
        return movies;
    }

    public Subtitle movies(Set<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public Subtitle addMovie(Movie movie) {
        this.movies.add(movie);
        movie.setSubtitle(this);
        return this;
    }

    public Subtitle removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.setSubtitle(null);
        return this;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
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

    public SubtitleLine getSubtitleLine() {
        return subtitleLine;
    }

    public Subtitle subtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLine = subtitleLine;
        return this;
    }

    public void setSubtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLine = subtitleLine;
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
