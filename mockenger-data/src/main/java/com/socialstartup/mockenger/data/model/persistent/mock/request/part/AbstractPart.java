package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dmitry Ryazanov on 3/31/2015.
 */
@ToString
public abstract class AbstractPart<T extends Transformer> {

    protected List<T> transformers;

    public List<T> getTransformers() {
        return Optional.ofNullable(transformers).orElse(new ArrayList<>());
    }

    public void setTransformers(List<T> transformers) {
        this.transformers = transformers;
    }
}
