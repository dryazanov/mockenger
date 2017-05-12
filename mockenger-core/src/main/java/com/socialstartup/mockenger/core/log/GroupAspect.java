package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.persistent.log.Event;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
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
public class GroupAspect extends CommonAspect {

    @Pointcut(EVENTABLE_ANNOTATION + " && args(group))")
    public void onGroupChanged(final Group group) {
		// Nothing to do here
	}


    @After("onGroupChanged(group)")
    public void createEvent(final JoinPoint joinPoint, final Group group) {
		save(Event.<Group>builder().eventType(getEventType(joinPoint)).entity(group));
    }
}
