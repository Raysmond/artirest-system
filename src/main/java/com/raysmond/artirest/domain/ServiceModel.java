package com.raysmond.artirest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raysmond.artirest.domain.enumeration.ServiceType;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Raysmond<i@raysmond.com>
 */
@XmlRootElement(name = "service")
public class ServiceModel {
    public String name;

    public String url;

    public RestMethod method;

    public String inputArtifact;

    public String outputArtifact;

    public String comment;

    public ServiceType type = ServiceType.HUMAN_TASK;

    public Set<String> inputParams = new HashSet<>();

    @XmlRootElement
    public static enum RestMethod {
        GET, PUT, POST, DELETE, PATCH
    }

    public ServiceModel() {

    }

    public ServiceModel(String name, String url, RestMethod method, String inputArtifact, String outputArtifact) {
        this.name = name;
        this.url = url;
        this.method = method;
        this.inputArtifact = inputArtifact;
        this.outputArtifact = outputArtifact;
    }

    public ServiceModel(String name, String url, RestMethod method, String inputArtifact, String outputArtifact, String comment) {
        this.name = name;
        this.url = url;
        this.method = method;
        this.inputArtifact = inputArtifact;
        this.outputArtifact = outputArtifact;
        this.comment = comment;
    }
}
