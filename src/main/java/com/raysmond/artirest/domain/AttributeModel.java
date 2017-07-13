package com.raysmond.artirest.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Raysmond<i@raysmond.com>
 */
@XmlRootElement(name = "attribute")
public class AttributeModel implements Serializable {

    private String name;

    private String comment;

    private String type;

    // 函数
    private String function;

    // 绑定WOT
    private String wot;

    public AttributeModel() {

    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getWot() {
        return wot;
    }

    public void setWot(String wot) {
        this.wot = wot;
    }

    public AttributeModel(String name, String type, String comment) {
        this.name = name;
        this.comment = comment;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
