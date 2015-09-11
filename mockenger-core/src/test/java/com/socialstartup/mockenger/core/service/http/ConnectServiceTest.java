package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.ConnectRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class ConnectServiceTest extends AbstractServiceTest {

    @InjectMocks
    private ConnectService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        ConnectRequest connectEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(connectEntity, RequestMethod.CONNECT);
    }
}
