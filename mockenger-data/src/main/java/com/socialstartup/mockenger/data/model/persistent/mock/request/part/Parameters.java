package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;

import java.util.List;
import java.util.Map;

/**
 * Created by x079089 on 3/31/2015.
 */
public class Parameters extends AbstractPart {

    private Map<String, String> values;

    public Parameters() {}

    public Parameters(Map<String, String> values) {
        this.values = values;
    }

    public Parameters(List<AbstractMapTransformer> transformers, Map<String, String> values) {
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
