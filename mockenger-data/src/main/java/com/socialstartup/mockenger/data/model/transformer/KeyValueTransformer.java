package com.socialstartup.mockenger.data.model.transformer;

/**
 * Created by x079089 on 3/22/2015.
 */
public class KeyValueTransformer extends AbstractTransformer implements IMapTransformer {

    private String key;

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

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }
}
