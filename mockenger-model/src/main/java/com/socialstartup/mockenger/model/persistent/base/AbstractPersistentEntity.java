package com.socialstartup.mockenger.model.persistent.base;

import com.socialstartup.mockenger.model.mock.request.AbstractMockRequest;
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
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMockRequest.class);

    @Id
    private ID id;

    public final ID getId() {
        return id;
    }

    public final void setId(ID id) {
        this.id = id;
    }
}
