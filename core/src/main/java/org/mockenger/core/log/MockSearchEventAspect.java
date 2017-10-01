package org.mockenger.core.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mockenger.data.model.dict.EventResultType;
import org.mockenger.data.model.dict.EventType;
import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class MockSearchEventAspect extends CommonAspect implements MockSearchPointcut {

	@Value("${mockenger.events.store.search.requests}")
	private boolean isEnabled;


	@Around("onMockSearch(genericRequest)")
	public Object createEvent(final ProceedingJoinPoint joinPoint, final GenericRequest genericRequest) throws Throwable {
		if (isEnabled) {
			final Event.EventBuilder<GenericRequest> builder = Event.<GenericRequest>builder()
					.eventType(EventType.SEARCH)
					.entity(genericRequest);

			try {
				final Object retVal = joinPoint.proceed();

				builder.resultType(Objects.nonNull(retVal) ? EventResultType.FOUND : EventResultType.NOT_FOUND);
				save(builder);

				return retVal;
			} catch (Throwable ex) {
				log.error("Error occurred during counter update for the request: " + genericRequest, ex);
				builder.resultType(EventResultType.FAILED);
				save(builder);

				throw ex;
			}
		}

		return joinPoint.proceed();
	}
}
