package org.mockenger.core.service.http;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.HeadRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class HeadServiceTest extends AbstractServiceTest {

    @InjectMocks
    private HeadService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        HeadRequest headEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(headEntity, RequestMethod.HEAD);
    }
}
