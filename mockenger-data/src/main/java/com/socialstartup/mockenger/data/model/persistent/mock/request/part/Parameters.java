package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;

import java.util.List;
import java.util.SortedSet;

/**
 * Created by x079089 on 3/31/2015.
 */
public class Parameters extends AbstractPart<AbstractMapTransformer> {

    private SortedSet<Pair> values;

    public Parameters() {}

    public Parameters(SortedSet<Pair> values) {
        setValues(values);
    }

    public Parameters(List<AbstractMapTransformer> transformers, SortedSet<Pair> values) {
        this.transformers = transformers;
        setValues(values);
    }

    public SortedSet<Pair> getValues() {
        return values;
    }

    public void setValues(SortedSet<Pair> values) {
        this.values = values;
    }
}
