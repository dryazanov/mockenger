package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.core.service.http.OptionsService;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.OptionsEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class OptionsServiceTest extends AbstractServiceTest {

    @InjectMocks
    private OptionsService classUnderTest;

    @Test
    public void testCreateMockRequest() {
        OptionsEntity optionsEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(optionsEntity, RequestType.OPTIONS);
    }
}
