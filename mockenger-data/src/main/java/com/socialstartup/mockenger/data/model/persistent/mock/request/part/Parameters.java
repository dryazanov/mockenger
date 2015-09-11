package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;

import java.util.List;
import java.util.Set;

/**
 * Created by Dmitry Ryazanov on 3/31/2015.
 */
public class Parameters extends AbstractPart<AbstractMapTransformer> {

    private Set<Pair> values;

    public Parameters() {}

    public Parameters(Set<Pair> values) {
        setValues(values);
    }

    public Parameters(List<AbstractMapTransformer> transformers, Set<Pair> values) {
        this.transformers = transformers;
        setValues(values);
    }

    public Set<Pair> getValues() {
        return values;
    }

    public void setValues(Set<Pair> values) {
        this.values = values;
    }
}
