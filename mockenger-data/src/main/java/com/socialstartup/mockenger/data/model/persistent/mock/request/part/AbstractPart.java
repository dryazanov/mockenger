package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.transformer.ITransformer;

import java.util.*;

/**
 * Created by x079089 on 3/31/2015.
 */
public abstract class AbstractPart {

    protected List<ITransformer> transformers;

    public List<ITransformer> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<ITransformer> transformers) {
        this.transformers = transformers;
    }
}
