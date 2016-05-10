package com.socialstartup.mockenger.data.model.persistent.log;

import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;

/**
 * Created by dryazanov on 06/04/16.
 */
public abstract class AbstractEventBuilder implements EventBuilder {

    protected String id;
    protected EventType eventType;
    protected Object entity;
    protected String username;
    protected EventResultType resultType = EventResultType.OK;

    public EventBuilder id(final String id) {
        this.id = id;
        return this;
    }

    public EventBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public EventBuilder entity(final Object entity) {
        this.entity = entity;
        return this;
    }

    public EventBuilder eventType(final EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventBuilder resultType(final EventResultType resultType) {
        this.resultType = resultType;
        return this;
    }
}
