package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;

import java.util.List;
import java.util.Set;

/**
 * Created by Dmitry Ryazanov on 3/31/2015.
 */
public class Headers extends AbstractPart<AbstractMapTransformer> {

    private Set<Pair> values;


    public Headers() {}

    public Headers(Set<Pair> values) {
        this.values = values;
    }

    public Headers(List<AbstractMapTransformer> transformers, Set<Pair> values) {
        this.transformers = transformers;
        this.values = values;
    }

    public Set<Pair> getValues() {
        return values;
    }

    public void setValues(Set<Pair> values) {
        this.values = values;
    }
}
