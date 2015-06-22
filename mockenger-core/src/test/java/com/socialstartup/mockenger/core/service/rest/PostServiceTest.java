package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.PostEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PostService classUnderTest;

    @Test
    public void testCreateMockRequestJson() throws IOException {
        PostEntity postEntity = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_DATA, httpServletRequestMock);
        checkEntityWithBody(postEntity, RequestType.POST, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestJsonWithSpaces() throws IOException {
        PostEntity postEntity = classUnderTest.createMockRequestFromJson(GROUP_ID, JSON_WITH_SPACES, httpServletRequestMock);
        checkEntityWithBody(postEntity, RequestType.POST, JSON_DATA);
    }

    @Test
    public void testCreateMockRequestXml() throws TransformerException {
        PostEntity postEntity = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_XML_DECLARATION, httpServletRequestMock, true);
        checkEntityWithBody(postEntity, RequestType.POST, XML_DATA);
    }

    @Test
    public void testCreateMockRequestXmlWithSpaces() throws TransformerException {
        PostEntity postEntity = classUnderTest.createMockRequestFromXml(GROUP_ID, XML_DATA_WITH_SPACES, httpServletRequestMock, true);
        checkEntityWithBody(postEntity, RequestType.POST, XML_DATA);
    }
}
