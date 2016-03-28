package com.raysmond.artirest.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author Raysmond<i@raysmond.com>
 */
public class Attribute implements Serializable{
    private String name;
    private String comment;

    @JsonSerialize
    private Object value;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Attribute() {

    }

    public Attribute(String name, String comment, Object value) {
        this.name = name;
        this.comment = comment;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "name='" + name + '\'' +
            ", comment='" + comment + '\'' +
            ", value=" + value +
            '}';
    }
}
