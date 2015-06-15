package com.socialstartup.mockenger.model.transformer;

/**
 * Created by x079089 on 3/22/2015.
 */
public interface ITransformer {

    TransformerType getType();

    void setType(TransformerType type);

    String getPattern();

    void setPattern(String pattern);

    String getReplacement();

    void setReplacement(String replacement);

    String transform(String source);
}
