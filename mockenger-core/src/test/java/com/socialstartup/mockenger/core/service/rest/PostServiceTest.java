package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PostService classUnderTest;

    @Test
    public void testCreateMockRequestJson() throws IOException {
        PostRequest postRequest = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(postRequest, RequestMethod.POST, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestJsonWithSpaces() throws IOException {
        PostRequest postRequest = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(postRequest, RequestMethod.POST, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestXml() throws TransformerException {
        PostRequest postRequest = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_XML_DECLARATION, httpServletRequestMock);
        checkEntityWithBody(postRequest, RequestMethod.POST, XML_DATA);
    }

    @Test
    public void testCreateMockRequestXmlWithSpaces() throws TransformerException {
        PostRequest postRequest = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(postRequest, RequestMethod.POST, XML_DATA);
    }
}
