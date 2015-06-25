package com.socialstartup.mockenger.data.model.dict;


/**
 * Created by x079089 on 3/12/2015.
 */
public enum ProjectType {
    RESTL("REST"),
    SOAP("SOAP"),
    SIMPLE("Simple");


    private String type;

    private ProjectType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
