package com.socialstartup.mockenger.model.mock.request.part;

import com.socialstartup.mockenger.model.transformer.IMapTransformer;

import java.util.*;

/**
 * Created by x079089 on 3/31/2015.
 */
public class Parameters extends AbstractPart {

    private Map<String, String> values;

    public Parameters() {}

    public Parameters(Map<String, String> values) {
        this.values = values;
    }

    public Parameters(List<IMapTransformer> transformers, Map<String, String> values) {
        this.transformers = transformers;
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
