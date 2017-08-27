package org.mockenger.data.model.dict;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

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
		return Stream.of(values()).collect(toMap(m -> m.name(), m -> m.name()));
    }
}
