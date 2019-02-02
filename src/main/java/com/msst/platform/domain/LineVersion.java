package com.msst.platform.domain;


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
 * A LineVersion.
 */
@Document(collection = "line_version")
public class LineVersion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("version")
    private String version;

    @Field("text")
    private String text;

    @DBRef
    @Field("subtitleLine")
    private Set<SubtitleLine> subtitleLines = new HashSet<>();

    @DBRef
    @Field("lineVersionRating")
    @JsonIgnoreProperties("lineVersions")
    private LineVersionRating lineVersionRating;

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

    public LineVersion version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public LineVersion text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<SubtitleLine> getSubtitleLines() {
        return subtitleLines;
    }

    public LineVersion subtitleLines(Set<SubtitleLine> subtitleLines) {
        this.subtitleLines = subtitleLines;
        return this;
    }

    public LineVersion addSubtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLines.add(subtitleLine);
        subtitleLine.setLineVersion(this);
        return this;
    }

    public LineVersion removeSubtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLines.remove(subtitleLine);
        subtitleLine.setLineVersion(null);
        return this;
    }

    public void setSubtitleLines(Set<SubtitleLine> subtitleLines) {
        this.subtitleLines = subtitleLines;
    }

    public LineVersionRating getLineVersionRating() {
        return lineVersionRating;
    }

    public LineVersion lineVersionRating(LineVersionRating lineVersionRating) {
        this.lineVersionRating = lineVersionRating;
        return this;
    }

    public void setLineVersionRating(LineVersionRating lineVersionRating) {
        this.lineVersionRating = lineVersionRating;
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
        LineVersion lineVersion = (LineVersion) o;
        if (lineVersion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lineVersion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LineVersion{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
