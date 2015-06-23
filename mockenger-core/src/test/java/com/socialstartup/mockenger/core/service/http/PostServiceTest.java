package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PostService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        PostRequest postRequest = classUnderTest.createMockRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(postRequest, RequestMethod.POST, JSON_DATA);
    }
}
