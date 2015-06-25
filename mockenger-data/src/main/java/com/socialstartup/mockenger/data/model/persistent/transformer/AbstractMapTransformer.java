package com.socialstartup.mockenger.data.model.persistent.transformer;

/**
 * Created by ydolzhenko on 25.06.15.
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
