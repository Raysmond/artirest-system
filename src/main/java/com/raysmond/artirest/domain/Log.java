package com.raysmond.artirest.domain;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

import com.raysmond.artirest.domain.enumeration.LogType;

/**
 * A Log.
 */

@Document(collection = "log")
public class Log implements Serializable {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("process_id")
    private String processId;

    @Field("artifact_id")
    private String artifactId;

    @Field("from_state")
    private String fromState;

    @Field("to_state")
    private String toState;

    @Field("service")
    private String service;

    @Field("memo")
    private String memo;

    @Field("created_at")
    @CreatedDate
    private ZonedDateTime createdAt;

    @Field("type")
    private LogType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Log log = (Log) o;
        return Objects.equals(id, log.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Log{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", processId='" + processId + "'" +
            ", artifactId='" + artifactId + "'" +
            ", fromState='" + fromState + "'" +
            ", toState='" + toState + "'" +
            ", service='" + service + "'" +
            ", memo='" + memo + "'" +
            ", createdAt='" + createdAt + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
