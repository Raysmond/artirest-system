package com.raysmond.artirest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

@Document(collection = "artifact_model")
public class ArtifactModel implements Serializable {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("comment")
    private String comment;

    @Field("created_at")
    @CreatedDate
    private ZonedDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    public Set<AttributeModel> attributes = new HashSet<>();

    @XmlElementWrapper(name = "states")
    @XmlElement(name = "state")
    public Set<StateModel> states = new HashSet<>();

    @XmlTransient
    @JsonIgnore
    public StateModel getStartState() {
        for (StateModel state : this.states) {
            if (state.type == StateModel.StateType.START) {
                return state;
            }
        }

        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtifactModel artifactModel = (ArtifactModel) o;
        return Objects.equals(id, artifactModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ArtifactModel{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", comment='" + comment + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            '}';
    }
}
