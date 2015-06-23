package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.TraceRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class TraceServiceTest extends AbstractServiceTest {

    @InjectMocks
    private TraceService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        TraceRequest traceEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(traceEntity, RequestMethod.TRACE);
    }
}
