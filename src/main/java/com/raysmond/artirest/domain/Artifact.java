package com.raysmond.artirest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

/**
 * A Artifact.
 */

@Document(collection = "artifact")
public class Artifact implements Serializable {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("current_state")
    private String currentState;

    @Field("created_at")
    @CreatedDate
    private ZonedDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @DBRef
    private ArtifactModel artifactModel;

    private Set<Attribute> attributes = new HashSet<>();

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

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
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

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArtifactModel getArtifactModel() {
        return artifactModel;
    }

    public void setArtifactModel(ArtifactModel artifactModel) {
        this.artifactModel = artifactModel;
    }

    public void setAttribute(String name, Attribute attr){
        boolean exist = false;
        for (Attribute _attr : this.attributes){
            if (_attr.getName().equals(name)){
                exist = true;
                _attr.setValue(attr.getValue());
                break;
            }
        }

        if (!exist){
            this.attributes.add(attr);
        }
    }

    @XmlTransient
    @JsonIgnore
    public Attribute getAttribute(String name){
        for (Attribute attr : this.attributes){
            if (attr.getName().equals(name)){
                return attr;
            }
        }

        return null;
    }


    public void updateAttribute(String name, Object value){
        Attribute attr = this.getAttribute(name);
        attr.setValue(value);
        this.setAttribute(name, attr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artifact artifact = (Artifact) o;
        return Objects.equals(id, artifact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Artifact{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", currentState='" + currentState + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", attributes=" + attributes +
            '}';
    }
}
