package com.msst.platform.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SubtitleLine.
 */
@Document(collection = "subtitle_line")
public class SubtitleLine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("start_time")
    private Duration startTime;

    @Field("end_time")
    private Duration endTime;

    @DBRef
    @Field("subtitle")
    private Set<Subtitle> subtitles = new HashSet<>();
    @DBRef
    @Field("lineVersion")
    @JsonIgnoreProperties("subtitleLines")
    private LineVersion lineVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Duration getStartTime() {
        return startTime;
    }

    public SubtitleLine startTime(Duration startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Duration startTime) {
        this.startTime = startTime;
    }

    public Duration getEndTime() {
        return endTime;
    }

    public SubtitleLine endTime(Duration endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Duration endTime) {
        this.endTime = endTime;
    }

    public Set<Subtitle> getSubtitles() {
        return subtitles;
    }

    public SubtitleLine subtitles(Set<Subtitle> subtitles) {
        this.subtitles = subtitles;
        return this;
    }

    public SubtitleLine addSubtitle(Subtitle subtitle) {
        this.subtitles.add(subtitle);
        subtitle.setSubtitleLine(this);
        return this;
    }

    public SubtitleLine removeSubtitle(Subtitle subtitle) {
        this.subtitles.remove(subtitle);
        subtitle.setSubtitleLine(null);
        return this;
    }

    public void setSubtitles(Set<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public LineVersion getLineVersion() {
        return lineVersion;
    }

    public SubtitleLine lineVersion(LineVersion lineVersion) {
        this.lineVersion = lineVersion;
        return this;
    }

    public void setLineVersion(LineVersion lineVersion) {
        this.lineVersion = lineVersion;
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
        SubtitleLine subtitleLine = (SubtitleLine) o;
        if (subtitleLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subtitleLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubtitleLine{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
