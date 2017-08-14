package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.HeadRequest;
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
