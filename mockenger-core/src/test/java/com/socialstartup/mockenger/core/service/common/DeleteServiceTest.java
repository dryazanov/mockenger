package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class DeleteServiceTest extends AbstractServiceTest {

    @InjectMocks
    private DeleteService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        DeleteRequest deleteEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(deleteEntity, RequestMethod.DELETE);
    }
}
