package com.socialstartup.mockenger.data.model.dict;

import java.util.Arrays;
import java.util.HashMap;
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
    OPTIONS,
    TRACE,
    CONNECT;

    public static Map<String, String> getValueSet() {
		final Map<String, String> valueset = new HashMap<>(RequestMethod.values().length);
        for (RequestMethod method : RequestMethod.values()) {
            valueset.put(method.name(), method.name());
        }
        return valueset;
    }
}
