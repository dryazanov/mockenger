package com.socialstartup.mockenger.data.model.persistent.transformer;

/**
 * Created by ydolzhenko on 25.06.15.
 */
public interface Transformer {

    String transform(String source) throws TransformerException;

}
