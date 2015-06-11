package org.mockenger.dev.model.transformer;

import org.mockenger.dev.model.error.TransformerExeption;

/**
 * Created by x079089 on 3/22/2015.
 */
public abstract class AbstractTransformer implements ITransformer {

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

    @Override
    public TransformerType getType() {
        return this.type;
    }

    @Override
    public void setType(TransformerType type) {
        this.type = type;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getReplacement() {
        return this.replacement;
    }

    @Override
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
