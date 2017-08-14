package org.mockenger.data.model.dict;


import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
public enum ProjectType {
    REST(Arrays.asList(RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE)),
    SOAP(Arrays.asList(RequestMethod.POST)),
    HTTP(Arrays.asList(RequestMethod.values()));

    private final List<RequestMethod> allowedMethods;


    ProjectType(final List<RequestMethod> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }


    public List<RequestMethod> getAllowedMethods() {
        return allowedMethods;
    }


    public static boolean contains(final String value) {
        for (ProjectType type : ProjectType.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
