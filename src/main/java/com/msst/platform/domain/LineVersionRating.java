package com.msst.platform.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LineVersionRating.
 */
@Document(collection = "line_version_rating")
public class LineVersionRating implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("rating")
    private Integer rating;

    @Field("comment")
    private String comment;

    @DBRef
    @Field("lineVersion")
    @JsonIgnoreProperties("ratings")
    private LineVersion lineVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public LineVersionRating rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public LineVersionRating comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LineVersion getLineVersion() {
        return lineVersion;
    }

    public LineVersionRating lineVersion(LineVersion lineVersion) {
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
        LineVersionRating lineVersionRating = (LineVersionRating) o;
        if (lineVersionRating.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lineVersionRating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LineVersionRating{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
