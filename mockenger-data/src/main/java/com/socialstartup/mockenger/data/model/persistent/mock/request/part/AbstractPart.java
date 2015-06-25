package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;

import java.util.List;

/**
 * Created by x079089 on 3/31/2015.
 */
public abstract class AbstractPart<T extends AbstractTransformer> {

    protected List<T> transformers;

    public List<T> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<T> transformers) {
        this.transformers = transformers;
    }
}
