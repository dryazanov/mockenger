package com.socialstartup.mockenger.data.model.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by x079089 on 3/22/2015.
 */
public class KeyValueTransformer extends AbstractTransformer implements IMapTransformer {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(KeyValueTransformer.class);

    private String key;


    public KeyValueTransformer() {
        setType(TransformerType.KEY_VALUE);
    }

    public KeyValueTransformer(String key, String pattern, String replacement) {
        this();
        setKey(key);
        setPattern(pattern);
        setReplacement(replacement);
    }

    @Override
    public String transform(String value) {
        validate();
        return value.replaceAll(this.pattern, this.replacement);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }
}
