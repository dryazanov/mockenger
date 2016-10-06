package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@ToString(callSuper = true)
public class Parameters extends AbstractPart<Transformer> {

    private Set<Pair> values;

    public Parameters() {}

    public Parameters(Set<Pair> values) {
        setValues(values);
    }

    public Parameters(final List<Transformer> transformers, final Set<Pair> values) {
        this.transformers = transformers;
        setValues(values);
    }

    public Set<Pair> getValues() {
        return ofNullable(values).orElse(new HashSet<>());
    }

    public void setValues(final Set<Pair> values) {
        this.values = values;
    }
}
