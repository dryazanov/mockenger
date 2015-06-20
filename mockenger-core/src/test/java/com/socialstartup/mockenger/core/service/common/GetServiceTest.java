package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.GetEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class GetServiceTest extends AbstractServiceTest {

    @InjectMocks
    private GetService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        GetEntity getEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(getEntity, RequestType.GET);
    }
}
