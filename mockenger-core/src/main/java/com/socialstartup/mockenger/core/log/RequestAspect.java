package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.persistent.log.RequestEvent;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Component
public class RequestAspect extends CommonAspect {


    @Pointcut(EVENTABLE_ANNOTATION + " && args(abstractRequest))")
    public void onRequestChanged(final AbstractRequest abstractRequest) {}


    @After("onRequestChanged(abstractRequest)")
    public void logAfterRequestChanged(final JoinPoint joinPoint, final AbstractRequest abstractRequest) {
        final RequestEvent event = fillUpEvent(RequestEvent.builder(), getEventType(joinPoint), abstractRequest).build();
        getEventService().save(event);
    }
}
