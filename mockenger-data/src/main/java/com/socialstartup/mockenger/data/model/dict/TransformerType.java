package com.socialstartup.mockenger.data.model.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by x079089 on 3/22/2015.
 */
public enum TransformerType {
    KEY_VALUE,
    REGEXP,
    XPATH;

    public static Map<String, String> getValueSet() {
        Map<String, String> valueset = new HashMap<>(TransformerType.values().length);
        for (TransformerType type : TransformerType.values()) {
            valueset.put(type.name(), type.name());
        }
        return valueset;
    }
}
