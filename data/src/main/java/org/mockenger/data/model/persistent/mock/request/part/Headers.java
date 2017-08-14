package org.mockenger.data.model.persistent.mock.request.part;

import org.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@ToString(callSuper = true)
public class Headers extends AbstractPart<Transformer> {

    private Set<Pair> values;


    public Headers() {}

    public Headers(Set<Pair> values) {
        this.values = values;
    }

    public Headers(final List<Transformer> transformers, final Set<Pair> values) {
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
