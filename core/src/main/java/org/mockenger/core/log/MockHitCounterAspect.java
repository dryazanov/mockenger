package org.mockenger.core.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Aspect
@Order(2)
@Component
public class MockHitCounterAspect implements MockSearchPointcut {

	@Autowired
	private RequestService requestService;


	@AfterReturning(value = "onMockSearch(genericRequest)", returning = "abstractRequest")
	public void increaseHitCounter(final JoinPoint joinPoint, final GenericRequest genericRequest, final AbstractRequest abstractRequest) {
		if (abstractRequest != null) {
			final ExecutorService executorService = Executors.newSingleThreadExecutor();

			try {
				executorService.submit(() -> requestService.updateRequestCounter(abstractRequest)).get();
			} catch (ExecutionException | InterruptedException ex) {
				log.error("Failed to update request counter for entity: " + abstractRequest, ex);
			} finally {
				executorService.shutdownNow();
			}
		}
	}
}
