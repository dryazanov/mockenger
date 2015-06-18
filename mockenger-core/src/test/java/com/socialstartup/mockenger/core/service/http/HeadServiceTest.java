package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.core.service.http.HeadService;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.HeadEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class HeadServiceTest extends AbstractServiceTest {

    @InjectMocks
    private HeadService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        HeadEntity headEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(headEntity, RequestType.HEAD);
    }
}
