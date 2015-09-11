package com.socialstartup.mockenger.data.model.persistent.transformer;

/**
 * Created by Dmitry Ryazanov on 3/23/2015.
 */
public class TransformerExeption extends RuntimeException {

    public TransformerExeption() {
        super();
    }

    public TransformerExeption(String message) {
        super(message);
    }
}
