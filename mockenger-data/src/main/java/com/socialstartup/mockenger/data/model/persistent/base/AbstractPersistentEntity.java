package com.socialstartup.mockenger.data.model.persistent.base;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by ydolzhenko on 15.06.15.
 */
public abstract class AbstractPersistentEntity<T extends Serializable> {

    @Id
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
