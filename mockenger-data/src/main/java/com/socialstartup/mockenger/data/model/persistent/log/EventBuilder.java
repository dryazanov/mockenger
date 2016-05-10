package com.socialstartup.mockenger.data.model.persistent.log;

import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;

/**
 * Created by dryazanov on 14/04/16.
 */
public interface EventBuilder {

    EventBuilder id(String id);

    EventBuilder entity(Object entity);

    EventBuilder eventType(EventType eventType);

    EventBuilder username(String username);

    EventBuilder resultType(EventResultType resultType);

    <T> T build();
}
