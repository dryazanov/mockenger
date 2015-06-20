package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.PutEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PutServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PutService classUnderTest;

    @Test
    public void testCreateMockRequestJson() throws IOException {
        PutEntity putEntity = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(putEntity, RequestType.PUT, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestJsonWithSpaces() throws IOException {
        PutEntity putEntity = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(putEntity, RequestType.PUT, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestXml() throws TransformerException {
        PutEntity putEntity = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA, httpServletRequestMock, true);
        checkEntityWithBody(putEntity, RequestType.PUT, XML_DATA);
    }

    @Test
    public void testCreateMockRequestXmlWithSpaces() throws TransformerException {
        PutEntity putEntity = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_SPACES, httpServletRequestMock, true);
        checkEntityWithBody(putEntity, RequestType.PUT, XML_DATA);
    }
}
