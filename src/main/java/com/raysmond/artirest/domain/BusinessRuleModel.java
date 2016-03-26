package com.raysmond.artirest.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Raysmond<i@raysmond.com>
 */
@XmlRootElement(name = "businessRule")
public class BusinessRuleModel implements Serializable {
    public String name;

//    public List<Condition> preConditions = new ArrayList<>();
//
//    public List<Condition> postConditions = new ArrayList<>();

    public Set<Atom> preConditions = new HashSet<>();

    public Set<Atom> postConditions = new HashSet<>();

    public Action action;

    @XmlRootElement(name = "condition")
    public static class Condition {
        public ConditionType type;

        @XmlElementWrapper(name = "atoms")
        @XmlElement(name = "atom")
        public Set<Atom> atoms = new HashSet<>();
    }

    @XmlRootElement
    public static enum ConditionType {
        AND, OR
    }

    /**
     * Atom types 1. instate(artifact, state) 2. defined(artifact, attribute) 3. Condition(artifact,
     * attribute, operator, value)
     */
    @XmlRootElement
    public static enum AtomType {
        INSTATE, ATTRIBUTE_DEFINED, SCALAR_COMPARISON
    }

    public static enum Operator{
        LESS, LARGER, EQUAL,
        LESS_EQUAL, LARGER_EQUAL
    }

    @XmlRootElement(name = "atom")
    public static class Atom {
        public String artifact;

        public String attribute;

        public String state;

        public Operator operator;

        public AtomType type;

        public Object value; // TODO value can have multiple types

        public Atom() {

        }

        public Atom(String artifact, String attribute, String state, Operator operator, AtomType type, Object value) {
            this.artifact = artifact;
            this.attribute = attribute;
            this.state = state;
            this.operator = operator;
            this.type = type;
            this.value = value;
        }
    }

    @XmlRootElement(name = "action")
    public static class Action {
        public String name;
        public String service;

        @XmlElementWrapper(name = "transitions")
        @XmlElement(name = "transition")
        public Set<Transition> transitions = new HashSet<>();

        public Action() {

        }

        public Action(String name, String service, Set<Transition> transitions) {
            this.name = name;
            this.service = service;
            this.transitions = transitions;
        }
    }

    @XmlRootElement(name = "transition")
    public static class Transition {
        public String artifact;

        public String fromState;

        public String toState;

        public Transition() {

        }

        public Transition(String artifact, String fromState, String toState) {
            this.artifact = artifact;
            this.fromState = fromState;
            this.toState = toState;
        }
    }
}
