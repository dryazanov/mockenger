package org.mockenger.core.service.rest;

import org.mockenger.core.service.AbstractServiceTest;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class PutServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PutService classUnderTest;

    @Test
    public void testCreateMockRequestJson() throws IOException {
        GenericRequest putRequest = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(putRequest, RequestMethod.PUT, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestJsonWithSpaces() throws IOException {
		GenericRequest putRequest = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(putRequest, RequestMethod.PUT, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestXml() throws TransformerException {
		GenericRequest putRequest = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_XML_DECLARATION, httpServletRequestMock);
        checkEntityWithBody(putRequest, RequestMethod.PUT, XML_DATA);
    }

    @Test
    public void testCreateMockRequestXmlWithSpaces() throws TransformerException {
		GenericRequest putRequest = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(putRequest, RequestMethod.PUT, XML_DATA);
    }
}
