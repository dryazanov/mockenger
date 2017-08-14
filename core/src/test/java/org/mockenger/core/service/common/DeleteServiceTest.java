package org.mockenger.core.service.common;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
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
