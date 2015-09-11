package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.data.model.dict.TransformerType;

/**
 * Created by Dmitry Ryazanov on 3/22/2015.
 */
public class RegexpTransformer extends AbstractTransformer {

    /**
     * Default constructor
     */
    public RegexpTransformer() {
        setType(TransformerType.REGEXP);
    }

    /**
     * Constructor with params
     *
     * @param pattern
     * @param replacement
     */
    public RegexpTransformer(String pattern, String replacement) {
        this();
        setPattern(pattern);
        setReplacement(replacement);
    }

    /**
     * Runs transformation against provided source
     *
     * @param source
     * @return
     */
    @Override
    public String transform(String source) {
        validate();
        return source.replaceAll(this.pattern, this.replacement);
    }
}
