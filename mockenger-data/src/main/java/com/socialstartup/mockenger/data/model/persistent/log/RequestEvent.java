package com.socialstartup.mockenger.data.model.persistent.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by dryazanov on 06/04/16.
 */
@Getter
@ToString(callSuper = true)
@Document(collection = "event")
public class RequestEvent extends Event {

    @DBRef
    private AbstractRequest entity;


    @JsonCreator
    public RequestEvent(@JsonProperty("id") final String id,
                        @JsonProperty("entity") final AbstractRequest entity,
                        @JsonProperty("eventType") final EventType eventType,
                        @JsonProperty("timestamp") final Date eventDate,
                        @JsonProperty("username") final String username,
                        @JsonProperty("resultType") final EventResultType resultType) {

        super(id, eventType, eventDate, username, resultType);
        this.entity = entity;
    }

    public static EventBuilder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractEventBuilder {
        @Override
        public RequestEvent build() {
            return new RequestEvent(this.id, (AbstractRequest)this.entity, this.eventType, new Date(), this.username, this.resultType);
        }
    }
}
