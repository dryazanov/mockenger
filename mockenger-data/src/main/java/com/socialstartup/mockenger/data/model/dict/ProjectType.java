package com.socialstartup.mockenger.data.model.dict;


import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
public enum ProjectType {
    REST(Arrays.asList(RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE)),
    SOAP(Arrays.asList(RequestMethod.POST)),
    HTTP(Arrays.asList(RequestMethod.values()));

    private final List<RequestMethod> allowedMethods;

    ProjectType(List<RequestMethod> allowedMethods) {
        this.allowedMethods = allowedMethods;
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
