package com.socialstartup.mockenger.data.model.persistent.transformer;

/**
 * @author Dmitry Ryazanov
 */
public abstract class AbstractMapTransformer extends AbstractTransformer {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
