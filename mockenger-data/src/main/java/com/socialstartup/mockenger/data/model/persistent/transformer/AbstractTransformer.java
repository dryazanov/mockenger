package com.socialstartup.mockenger.data.model.persistent.transformer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Setter
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
        final String className = this.getClass().getCanonicalName();

        if (this.pattern == null) {
            throw new TransformerException("Pattern in '" + className + "' is NULL");
        }

        if (this.replacement == null) {
            throw new TransformerException("Replacement in '" + className + "' is NULL");
        }
    }
}
