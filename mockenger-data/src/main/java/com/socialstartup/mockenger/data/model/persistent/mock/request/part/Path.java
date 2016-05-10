package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

/**
 * Created by Dmitry Ryazanov on 3/31/2015.
 */
@ToString(callSuper = true)
public class Path extends AbstractPart<AbstractTransformer> {

    private String value;

    public Path() {}

    public Path(String value) {
        this.value = value;
    }

    public Path(List<AbstractTransformer> transformers, String value) {
        this.transformers = transformers;
        setValue(value);
    }

    public String getValue() {
        return Optional.ofNullable(value).orElse("");
    }

    public void setValue(String value) {
        this.value = value;
    }
}
