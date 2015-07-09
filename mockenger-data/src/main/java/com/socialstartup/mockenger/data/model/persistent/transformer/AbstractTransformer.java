package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.socialstartup.mockenger.data.model.dict.TransformerType;

/**
 * Created by x079089 on 3/22/2015.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RegexpTransformer.class, name = "RegexpTransformer"),
    @JsonSubTypes.Type(value = XPathTransformer.class, name = "XPathTransformer"),
    @JsonSubTypes.Type(value = KeyValueTransformer.class, name = "KeyValueTransformer")
})
public abstract class AbstractTransformer implements Transformer {

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

}
