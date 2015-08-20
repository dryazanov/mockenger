package com.socialstartup.mockenger.data.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ydolzhenko on 15.06.15.
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
        Map<String, String> valueset = new HashMap<>(RequestMethod.values().length);
        for (RequestMethod method : RequestMethod.values()) {
            valueset.put(method.name(), method.name());
        }
        return valueset;
    }
}
