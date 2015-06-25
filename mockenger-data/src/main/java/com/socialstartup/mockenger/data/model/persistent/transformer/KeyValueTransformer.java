package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.data.model.dict.TransformerType;

/**
 * Created by x079089 on 3/22/2015.
 */
public class KeyValueTransformer extends AbstractMapTransformer {


    /**
     * Default constructor
     */
    public KeyValueTransformer() {
        setType(TransformerType.KEY_VALUE);
    }

    /**
     * Constructor with params
     *
     * @param key
     * @param pattern
     * @param replacement
     */
    public KeyValueTransformer(String key, String pattern, String replacement) {
        this();
        setKey(key);
        setPattern(pattern);
        setReplacement(replacement);
    }

    /**
     * Replaces value for specific key
     *
     * @param value
     * @return
     */
    @Override
    public String transform(String value) {
        validate();
        return value.replaceAll(this.pattern, this.replacement);
    }

}
