package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.socialstartup.mockenger.data.model.dict.TransformerType;

/**
 * Created by x079089 on 3/22/2015.
 */
public abstract class AbstractTransformer {

    protected TransformerType type;

    protected String pattern;

    protected String replacement;


    protected void validate() {
        if (this.pattern == null) {
            throw new TransformerExeption("Pattern in " + this.getClass().getCanonicalName() + " is NULL");
        }
        if (this.replacement == null) {
            throw new TransformerExeption("Replacement in " + this.getClass().getCanonicalName() + " is NULL");
        }
    }

    public TransformerType getType() {
        return this.type;
    }

    public void setType(TransformerType type) {
        this.type = type;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReplacement() {
        return this.replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    abstract String transform(String source);
}
