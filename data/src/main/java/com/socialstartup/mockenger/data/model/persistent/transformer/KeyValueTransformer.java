package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.data.model.dict.TransformerType;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
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
    public KeyValueTransformer(final String key, String pattern, final String replacement) {
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
    public String transform(final String value) {
        validate();

		return ofNullable(value)
				.map(s -> s.replaceFirst(this.pattern, this.replacement))
				.orElse("");
    }
}
