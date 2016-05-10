package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.persistent.log.ProjectEvent;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
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
    public void onProjectChanged(final Project project) {}


    @After("onProjectChanged(project)")
    public void logAfterProjectChanged(final JoinPoint joinPoint, final Project project) {
        final ProjectEvent event = fillUpEvent(ProjectEvent.builder(), getEventType(joinPoint), project).build();
        getEventService().save(event);
    }
}
