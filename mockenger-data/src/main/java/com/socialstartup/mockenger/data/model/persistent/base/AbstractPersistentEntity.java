package com.socialstartup.mockenger.data.model.persistent.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * Created by ydolzhenko on 15.06.15.
 */
public abstract class AbstractPersistentEntity<ID extends Serializable> {

    @Transient
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPersistentEntity.class);

    @Id
    private ID id;

    public final ID getId() {
        return id;
    }

    public final void setId(ID id) {
        this.id = id;
    }
}
