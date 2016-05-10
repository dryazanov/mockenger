package com.socialstartup.mockenger.data.model.persistent.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by dryazanov on 06/04/16.
 */
@Getter
@ToString(callSuper = true)
public class GroupEvent extends Event {

    private Group entity;


    @JsonCreator
    public GroupEvent(@JsonProperty("id") final String id,
                      @JsonProperty("entity") final Group entity,
                      @JsonProperty("eventType") final EventType eventType,
                      @JsonProperty("timestamp") final Date eventDate,
                      @JsonProperty("username") final String username,
                      @JsonProperty("resultType") final EventResultType resultType) {

        super(id, eventType, eventDate, username, resultType);
        this.entity = entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder
     */
    private static class Builder extends AbstractEventBuilder {
        @Override
        public GroupEvent build() {
            return new GroupEvent(this.id, (Group)this.entity, this.eventType, new Date(), this.username, this.resultType);
        }
    }
}
