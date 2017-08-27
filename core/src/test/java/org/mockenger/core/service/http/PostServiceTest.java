package org.mockenger.core.service.http;

import org.junit.Test;
import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockito.InjectMocks;

import static org.mockenger.data.model.dict.RequestMethod.POST;

/**
 * @author Dmitry Ryazanov
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PostService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        final PostRequest postRequest = classUnderTest.createGenericRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);

        checkEntityWithBody(postRequest, POST, JSON_DATA);
    }
}
