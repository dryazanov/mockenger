package org.mockenger.core.log;

import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
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
    public void onRequestChanged(final AbstractRequest abstractRequest) {
		// Nothing to do here
	}


    @After("onRequestChanged(abstractRequest)")
    public void createEvent(final JoinPoint joinPoint, final AbstractRequest abstractRequest) {
		save(Event.<AbstractRequest>builder().eventType(getEventType(joinPoint)).entity(abstractRequest));
    }
}
