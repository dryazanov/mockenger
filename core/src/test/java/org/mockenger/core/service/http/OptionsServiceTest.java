package org.mockenger.core.service.http;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class OptionsServiceTest extends AbstractServiceTest {

    @InjectMocks
    private OptionsService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        OptionsRequest optionsEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(optionsEntity, RequestMethod.OPTIONS);
    }
}
