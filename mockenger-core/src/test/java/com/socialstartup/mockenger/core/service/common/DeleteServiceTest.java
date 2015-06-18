package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.DeleteEntity;
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
        DeleteEntity deleteEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(deleteEntity, RequestType.DELETE);
    }
}
