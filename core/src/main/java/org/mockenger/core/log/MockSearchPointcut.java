package org.mockenger.core.log;

import org.aspectj.lang.annotation.Pointcut;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;


/**
 * @author Dmitry Ryazanov
 */
public interface MockSearchPointcut {

	@Pointcut("execution(* *.findMockedEntities(..)) && args(genericRequest)")
	default void onMockSearch(final GenericRequest genericRequest) {}
}
