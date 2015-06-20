package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.PatchEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PatchServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PatchService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        PatchEntity patchEntity = classUnderTest.createMockRequest(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(patchEntity, RequestType.PATCH, JSON_DATA);
    }
}
