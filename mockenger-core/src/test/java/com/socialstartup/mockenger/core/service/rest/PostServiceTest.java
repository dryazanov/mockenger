package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.PutEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.io.IOException;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PutService classUnderTest;

    @Test
    public void testCreateMockRequest() throws IOException {
        PutEntity putEntity = classUnderTest.createMockRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(putEntity, RequestType.PUT, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestWithSpaces() throws IOException {
        PutEntity putEntity = classUnderTest.createMockRequest(GROUP_ID, JSON_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(putEntity, RequestType.PUT, JSON_DATA);
    }
}
