package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
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

    @Pointcut("execution(* *.findMockedEntities(..)) && args(genericRequest)")
    public void onMockSearch(final GenericRequest genericRequest) {}


    @Around("onMockSearch(genericRequest)")
    public Object createEvent(final ProceedingJoinPoint joinPoint, final GenericRequest genericRequest) throws Throwable {
        final Event.EventBuilder<GenericRequest> builder = Event.<GenericRequest>builder()
				.eventType(EventType.SEARCH)
				.entity(genericRequest);

        try {
            final Object retVal = joinPoint.proceed();

            if (retVal != null) {
                builder.resultType(EventResultType.FOUND);
            } else {
                builder.resultType(EventResultType.NOT_FOUND);
            }

			save(builder);

            return retVal;
        } catch (Throwable ex) {
            builder.resultType(EventResultType.FAILED);
			save(builder);

            throw ex;
        }
    }
}
