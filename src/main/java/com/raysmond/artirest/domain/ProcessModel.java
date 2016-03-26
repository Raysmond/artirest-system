package com.raysmond.artirest.domain;

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

import com.raysmond.artirest.domain.enumeration.Status;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * A ProcessModel.
 */

@Document(collection = "process_model")
public class ProcessModel implements Serializable {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("comment")
    private String comment;

    @Field("status")
    private Status status;

    @Field("created_at")
    @CreatedDate
    private ZonedDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @DBRef
    @XmlElementWrapper(name = "artifacts")
    @XmlElement(name = "artifact")
    public Set<ArtifactModel> artifacts = new HashSet<>();

    @XmlElementWrapper(name = "services")
    @XmlElement(name = "service")
    public Set<ServiceModel> services = new HashSet<>();

    @XmlElementWrapper(name = "rules")
    @XmlElement(name = "rule")
    public Set<BusinessRuleModel> businessRules = new HashSet<>();

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        ProcessModel processModel = (ProcessModel) o;
        return Objects.equals(id, processModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProcessModel{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", comment='" + comment + "'" +
            ", status='" + status + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            '}';
    }
}
