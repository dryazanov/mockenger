package org.mockenger.core.service.http;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * @author  Dmitry Ryazanov
 */
public class OptionsServiceTest extends AbstractServiceTest {

    @InjectMocks
    private OptionsService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        final OptionsRequest optionsEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);

        checkEntityWithoutBody(optionsEntity, RequestMethod.OPTIONS);
    }
}
