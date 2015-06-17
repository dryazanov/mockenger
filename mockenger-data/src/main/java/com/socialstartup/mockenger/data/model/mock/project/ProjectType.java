package com.socialstartup.mockenger.data.model.mock.project;

/**
 * Created by x079089 on 3/12/2015.
 */
public enum ProjectType {
    RESTFUL("RESTFul"),
    SIMPLE("Simple");

    private String typeName;

    private ProjectType(String name) {
        this.typeName = name;
    }

    public String getTypeName() {
        return typeName;
    }
}
