package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.core.service.EventService;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.log.EventBuilder;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * @author Dmitry Ryazanov
 */
@Component
public class CommonAspect {

    public static final String UNKNOWN_EVENT_TYPE = "unknown";

    public static final String EVENTABLE_ANNOTATION = "@annotation(com.socialstartup.mockenger.data.model.persistent.log.Eventable)";

    @Value("${spring.profiles.active}")
    private String securityProfile;

    @Autowired
    private EventService eventService;

    @Autowired
    private HttpServletRequest request;


    protected EventBuilder fillUpEvent(final EventBuilder eventBuilder, final EventType eventType, final Object entity) {
        eventBuilder.eventType(eventType).entity(entity);

        if (!StringUtils.isEmpty(securityProfile)) {
            eventBuilder.username(SecurityContextHolder.getContext().getAuthentication().getName());
        }

        return eventBuilder;
    }

    protected EventType getEventType(final JoinPoint joinPoint) {
        return EventType.getType(Optional.ofNullable(joinPoint.getSignature().getName()).orElse(""));
    }

    protected EventService getEventService() {
        return eventService;
    }
}
