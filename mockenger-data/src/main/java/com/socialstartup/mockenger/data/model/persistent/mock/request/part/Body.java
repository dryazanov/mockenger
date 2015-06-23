package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.transformer.ITransformer;

import java.util.*;

/**
 * Created by x079089 on 3/31/2015.
 */
public class Body extends AbstractPart {

    private String value;

    public Body() {}

    public Body(String value) {
        this.value = value;
    }

    public Body(List<ITransformer> transformers, String value) {
        this.transformers = transformers;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
