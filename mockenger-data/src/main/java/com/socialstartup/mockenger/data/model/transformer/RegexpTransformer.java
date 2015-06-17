package com.socialstartup.mockenger.data.model.transformer;

import org.slf4j.*;

/**
 * Created by x079089 on 3/22/2015.
 */
public class RegexpTransformer extends AbstractTransformer {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RegexpTransformer.class);


    public RegexpTransformer() {
        setType(TransformerType.REGEXP);
    }

    public RegexpTransformer(String pattern, String replacement) {
        this();
        setPattern(pattern);
        setReplacement(replacement);
    }

    @Override
    public String transform(String source) {
        validate();
        return source.replaceAll(this.pattern, this.replacement);
    }
}
