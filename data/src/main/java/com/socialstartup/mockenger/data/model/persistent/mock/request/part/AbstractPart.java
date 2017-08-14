package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

/**
 * @author Dmitry Ryazanov
 */
@ToString
public abstract class AbstractPart<T extends Transformer> {

    protected List<T> transformers;

    public List<T> getTransformers() {
        return ofNullable(transformers).orElse(emptyList());
    }

    public void setTransformers(final List<T> transformers) {
        this.transformers = transformers;
    }
}
