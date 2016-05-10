package com.socialstartup.mockenger.data.model.persistent.log;

import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by dryazanov on 06/04/16.
 */
@Getter
@ToString
public class Event {

    @Id
    private String id;

    protected EventType eventType;

    protected Date eventDate;

    protected String username;

    protected EventResultType resultType;


    /**
     * Default constructor
     */
    private Event() {}

    public Event(final String id, final EventType eventType, final Date eventDate, final String username, final EventResultType resultType) {
        this.id = id;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.username = username;
        this.resultType = resultType;
    }
}
