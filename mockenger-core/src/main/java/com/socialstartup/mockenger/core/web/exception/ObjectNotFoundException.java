package com.socialstartup.mockenger.core.web.exception;

import com.socialstartup.mockenger.data.model.persistent.base.AbstractPersistentEntity;

/**
 * Created by x079089 on 6/19/2015.
 */
public class ObjectNotFoundException extends RuntimeException {

    private AbstractPersistentEntity clazz;

    private String itemId;

    public ObjectNotFoundException(AbstractPersistentEntity clazz, String itemId) {
        this.clazz = clazz;
        this.itemId = itemId;
    }

    public AbstractPersistentEntity getClazz() {
        return clazz;
    }

    public void setClazz(AbstractPersistentEntity clazz) {
        this.clazz = clazz;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
