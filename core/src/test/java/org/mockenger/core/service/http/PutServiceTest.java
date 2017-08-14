package org.mockenger.core.service.http;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class PutServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PutService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        PutRequest putRequest = classUnderTest.createMockRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(putRequest, RequestMethod.PUT, JSON_DATA);
    }
}
