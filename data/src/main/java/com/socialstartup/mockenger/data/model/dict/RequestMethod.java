package com.socialstartup.mockenger.data.model.dict;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Dmitry Ryazanov
 */
public enum RequestMethod {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS;

    public static Map<String, String> getValueSet() {
		return Arrays.stream(RequestMethod.values()).collect(Collectors.toMap(m -> m.name(), m -> m.name()));
    }
}
