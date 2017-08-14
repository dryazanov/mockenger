package com.socialstartup.mockenger.core.log;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.dict.EventResultType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Component
public class MockSearchAspect extends CommonAspect {

	@Autowired
	private RequestService requestService;


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
				final ExecutorService executorService = Executors.newSingleThreadExecutor();
				final AbstractRequest abstractRequest = (AbstractRequest) retVal;

				try {
					executorService.submit(() -> requestService.updateRequestCounter(abstractRequest)).get();
				} catch (ExecutionException | InterruptedException ex) {
					log.error("Failed to update request counter for entity: " + abstractRequest, ex);
				} finally {
					executorService.shutdownNow();
				}

                builder.resultType(EventResultType.FOUND);
            } else {
                builder.resultType(EventResultType.NOT_FOUND);
			}

			save(builder);

			return retVal;
		} catch (Throwable ex) {
			log.error("Error occurred during counter update for the request: " + genericRequest, ex);
            builder.resultType(EventResultType.FAILED);
			save(builder);

			throw ex;
		}
	}
}
