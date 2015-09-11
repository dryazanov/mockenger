package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class GetServiceTest extends AbstractServiceTest {

    @InjectMocks
    private GetService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        GetRequest getEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(getEntity, RequestMethod.GET);
    }
}
