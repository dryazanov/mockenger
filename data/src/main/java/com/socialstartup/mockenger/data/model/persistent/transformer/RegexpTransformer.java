package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.data.model.dict.TransformerType;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
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
    public RegexpTransformer(final String pattern, final String replacement) {
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
    public String transform(final String source) {
        validate();

		return ofNullable(source)
				.map(s -> s.replaceAll(this.pattern, this.replacement))
				.orElse("");
    }
}
