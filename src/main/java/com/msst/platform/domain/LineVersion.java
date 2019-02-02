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
    @JsonIgnoreProperties("versions")
    private SubtitleLine subtitleLine;

    @DBRef
    @Field("ratings")
    private Set<LineVersionRating> ratings = new HashSet<>();
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

    public SubtitleLine getSubtitleLine() {
        return subtitleLine;
    }

    public LineVersion subtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLine = subtitleLine;
        return this;
    }

    public void setSubtitleLine(SubtitleLine subtitleLine) {
        this.subtitleLine = subtitleLine;
    }

    public Set<LineVersionRating> getRatings() {
        return ratings;
    }

    public LineVersion ratings(Set<LineVersionRating> lineVersionRatings) {
        this.ratings = lineVersionRatings;
        return this;
    }

    public LineVersion addRatings(LineVersionRating lineVersionRating) {
        this.ratings.add(lineVersionRating);
        lineVersionRating.setLineVersion(this);
        return this;
    }

    public LineVersion removeRatings(LineVersionRating lineVersionRating) {
        this.ratings.remove(lineVersionRating);
        lineVersionRating.setLineVersion(null);
        return this;
    }

    public void setRatings(Set<LineVersionRating> lineVersionRatings) {
        this.ratings = lineVersionRatings;
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
