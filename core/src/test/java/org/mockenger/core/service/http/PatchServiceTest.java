package org.mockenger.core.service.http;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.PatchRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class PatchServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PatchService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        PatchRequest patchRequest = classUnderTest.createMockRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(patchRequest, RequestMethod.PATCH, JSON_DATA);
    }
}
