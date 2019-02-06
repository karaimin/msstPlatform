package com.msst.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SubtitleLine.
 */
@Document(collection = "subtitle_line")
public class SubtitleLine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("sequence_number")
    private int sequenceNumber;

    @Field("start_time")
    private Duration startTime;

    @Field("end_time")
    private Duration endTime;

    @DBRef
    @Field("subtitle")
    @JsonIgnoreProperties("lines")
    private Subtitle subtitle;

    @Field("versions")
    private Set<LineVersion> versions = new HashSet<>();
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

    public Subtitle getSubtitle() {
        return subtitle;
    }

    public SubtitleLine subtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void setSubtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
    }

    public Set<LineVersion> getVersions() {
        return versions;
    }

    public SubtitleLine versions(Set<LineVersion> lineVersions) {
        this.versions = lineVersions;
        return this;
    }

    public SubtitleLine addVersions(LineVersion lineVersion) {
        this.versions.add(lineVersion);
        return this;
    }

    public SubtitleLine removeVersions(LineVersion lineVersion) {
        this.versions.remove(lineVersion);
        return this;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setVersions(Set<LineVersion> lineVersions) {
        this.versions = lineVersions;
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
