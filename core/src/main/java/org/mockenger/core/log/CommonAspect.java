package org.mockenger.core.log;

import org.mockenger.core.service.EventService;
import org.mockenger.data.model.dict.EventType;
import org.mockenger.data.model.persistent.log.Event;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


/**
 * @author Dmitry Ryazanov
 */
@Component
public class CommonAspect {

    public static final String EVENTABLE_ANNOTATION = "@annotation(org.mockenger.data.model.persistent.log.Eventable)";

    @Autowired
    private EventService eventService;


	protected void save(final Event.EventBuilder builder) {
		builder.username(getUsername()).eventDate(new Date());
		getEventService().save(builder.build());
	}


    protected EventType getEventType(final JoinPoint joinPoint) {
        return EventType.getType(Optional.ofNullable(joinPoint.getSignature().getName()).orElse(""));
    }


    protected EventService getEventService() {
        return eventService;
    }


    private String getUsername() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
			return authentication.getName();
        }

        return null;
    }
}
