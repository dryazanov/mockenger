package org.mockenger.data.model.persistent.mock.request.part;

import org.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@ToString(callSuper = true)
public class Path extends AbstractPart<Transformer> {

    private String value;

    public Path() {}

    public Path(final String value) {
        this.value = value;
    }

    public Path(final List<Transformer> transformers, final String value) {
        this.transformers = transformers;
        setValue(value);
    }

    public String getValue() {
        return ofNullable(value).orElse("");
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
