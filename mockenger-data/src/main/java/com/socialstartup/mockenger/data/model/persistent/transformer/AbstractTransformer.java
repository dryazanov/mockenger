package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.socialstartup.mockenger.data.model.dict.TransformerType;

/**
 * Created by Dmitry Ryazanov on 3/22/2015.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RegexpTransformer.class, name = "REGEXP"),
    @JsonSubTypes.Type(value = XPathTransformer.class, name = "XPATH"),
    @JsonSubTypes.Type(value = KeyValueTransformer.class, name = "KEY_VALUE")
})
public abstract class AbstractTransformer implements Transformer {

    protected TransformerType type;

    protected String pattern;

    protected String replacement;


    protected void validate() {
        String className = this.getClass().getCanonicalName();
        if (this.pattern == null) {
            throw new TransformerException(String.format("Pattern in %s is NULL", className));
        }
        if (this.replacement == null) {
            throw new TransformerException(String.format("Replacement in %s is NULL", className));
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

}
