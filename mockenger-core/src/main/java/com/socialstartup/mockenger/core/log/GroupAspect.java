package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.persistent.log.GroupEvent;
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
    public void onGroupChanged(final Group group) {}


    @After("onGroupChanged(group)")
    public void logAfterGroupChanged(final JoinPoint joinPoint, final Group group) {
        final GroupEvent event = fillUpEvent(GroupEvent.builder(), getEventType(joinPoint), group).build();
        getEventService().save(event);
    }
}
