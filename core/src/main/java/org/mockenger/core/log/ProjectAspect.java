package org.mockenger.core.log;

import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.model.persistent.mock.project.Project;
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
public class ProjectAspect extends CommonAspect {

    @Pointcut(EVENTABLE_ANNOTATION + " && args(project))")
    public void onProjectChanged(final Project project) {
		// Nothing to do here
	}


    @After("onProjectChanged(project)")
    public void createEvent(final JoinPoint joinPoint, final Project project) {
		save(Event.<Project>builder().eventType(getEventType(joinPoint)).entity(project));
    }
}
