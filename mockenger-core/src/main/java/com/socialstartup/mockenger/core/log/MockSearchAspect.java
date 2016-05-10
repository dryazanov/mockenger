package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.log.EventBuilder;
import com.socialstartup.mockenger.data.model.persistent.log.RequestEvent;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Component
public class MockSearchAspect extends CommonAspect {

    @Pointcut("execution(* *.findMockedEntities(..)) && args(abstractRequest)")
    public void onMockSearch(final AbstractRequest abstractRequest) {}


    @Around("onMockSearch(abstractRequest)")
    public Object logAroundMockSearch(final ProceedingJoinPoint joinPoint, final AbstractRequest abstractRequest) throws Throwable {
        final EventBuilder builder = fillUpEvent(RequestEvent.builder(), EventType.SEARCH, abstractRequest);

        try {
            final Object retVal = joinPoint.proceed();

            if (retVal != null) {
                builder.resultType(EventResultType.FOUND);
            } else {
                builder.resultType(EventResultType.NOT_FOUND);
            }

            saveRequestEvent(builder);

            return retVal;
        } catch (Throwable ex) {
            builder.resultType(EventResultType.FAILED);
            saveRequestEvent(builder);

            throw ex;
        }
    }

    private void saveRequestEvent(final EventBuilder builder) {
        try {
            getEventService().save(builder.build());
        } catch (Exception ex) {
            log.error("Cannot save request event", ex);
        }
    }
}
