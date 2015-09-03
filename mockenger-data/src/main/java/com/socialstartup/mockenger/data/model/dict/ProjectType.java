package com.socialstartup.mockenger.data.model.dict;


import java.util.Arrays;
import java.util.List;

/**
 * Created by x079089 on 3/12/2015.
 */
public enum ProjectType {
    REST("REST", Arrays.asList(RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE)),
    SOAP("SOAP", Arrays.asList(RequestMethod.POST)),
    SIMPLE("Simple", Arrays.asList(RequestMethod.values()));

    private String type;

    private List<RequestMethod> allowedMethods;

    private ProjectType(String type, List<RequestMethod> allowedMethods) {
        this.type = type;
        this.allowedMethods = allowedMethods;
    }

    public String getType() {
        return type;
    }

    public List<RequestMethod> getAllowedMethods() {
        return allowedMethods;
    }

    public static boolean contains(String value) {
        for (ProjectType type : ProjectType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
