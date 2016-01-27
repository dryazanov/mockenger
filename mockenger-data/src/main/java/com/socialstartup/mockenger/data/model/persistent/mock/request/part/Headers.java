package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        setValues(values);
    }

    public Set<Pair> getValues() {
        return Optional.ofNullable(values).orElse(new HashSet<>());
    }

    public void setValues(Set<Pair> values) {
        this.values = values;
    }
}
