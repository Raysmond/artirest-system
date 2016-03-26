package com.raysmond.artirest.domain;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Raysmond<i@raysmond.com>
 */

@XmlRootElement(name = "state")
public class StateModel {
    public String name;

    public String comment;

    public StateType type;

    @XmlRootElement
    public static enum StateType {
        START, NORMAL, FINAL
    }

    @XmlElementWrapper(name = "nextStates")
    public Set<String> nextStates = new HashSet<>();

    public StateModel() {

    }

    public StateModel(String name, String comment, StateType type) {
        this.name = name;
        this.comment = comment;
        this.type = type;
    }
}
