package org.mockenger.data.model.persistent.transformer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Dmitry Ryazanov
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = RegexpTransformer.class, name = "REGEXP"),
		@JsonSubTypes.Type(value = XPathTransformer.class, name = "XPATH"),
		@JsonSubTypes.Type(value = KeyValueTransformer.class, name = "KEY_VALUE")
})
public interface Transformer {

    String transform(String source) throws TransformerException;

}
