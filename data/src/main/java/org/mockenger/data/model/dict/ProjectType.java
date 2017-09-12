package org.mockenger.data.model.dict;


import java.util.Arrays;
import java.util.List;

import static org.mockenger.data.model.dict.RequestMethod.DELETE;
import static org.mockenger.data.model.dict.RequestMethod.GET;
import static org.mockenger.data.model.dict.RequestMethod.POST;
import static org.mockenger.data.model.dict.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
public enum ProjectType {
    HTTP(Arrays.asList(RequestMethod.values())),
    REST(Arrays.asList(GET, POST, PUT, DELETE)),
    SOAP(Arrays.asList(POST));

    private final List<RequestMethod> allowedMethods;


    ProjectType(final List<RequestMethod> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }


    public List<RequestMethod> getAllowedMethods() {
        return allowedMethods;
    }


    public static boolean contains(final String value) {
        for (ProjectType type : values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
