package com.msst.platform.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A LineVersion.
 */
public class LineVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("version")
    private String version;

    @Field("text")
    private String text;

    @DBRef
    @Field("ratings")
    private Set<LineVersionRating> ratings = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    public Set<LineVersionRating> getRatings() {
        return ratings;
    }

    public LineVersion ratings(Set<LineVersionRating> lineVersionRatings) {
        this.ratings = lineVersionRatings;
        return this;
    }

    public LineVersion addRatings(LineVersionRating lineVersionRating) {
        this.ratings.add(lineVersionRating);
        return this;
    }

    public LineVersion removeRatings(LineVersionRating lineVersionRating) {
        this.ratings.remove(lineVersionRating);
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
        if (lineVersion.getVersion() == null || getVersion() == null) {
            return false;
        }
        return Objects.equals(getVersion(), lineVersion.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVersion());
    }

    @Override
    public String toString() {
        return "LineVersion{" +
            "version='" + getVersion() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
