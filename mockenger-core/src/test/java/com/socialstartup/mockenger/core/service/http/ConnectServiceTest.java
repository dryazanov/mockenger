package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.ConnectEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class ConnectServiceTest extends AbstractServiceTest {

    @InjectMocks
    private ConnectService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        ConnectEntity connectEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(connectEntity, RequestType.CONNECT);
    }
}
