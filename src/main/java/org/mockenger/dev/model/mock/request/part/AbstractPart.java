package org.mockenger.dev.model.mock.request.part;

import java.util.*;

/**
 * Created by x079089 on 3/31/2015.
 */
public abstract class AbstractPart<T> {

    protected List<T> transformers;

    public List<T> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<T> transformers) {
        this.transformers = transformers;
    }
}
