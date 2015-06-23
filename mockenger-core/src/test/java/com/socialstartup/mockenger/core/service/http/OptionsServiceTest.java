package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.OptionsRequest;
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
        OptionsRequest optionsEntity = classUnderTest.createMockRequest(GROUP_ID, httpServletRequestMock);
        checkEntityWithoutBody(optionsEntity, RequestMethod.OPTIONS);
    }
}
