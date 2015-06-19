package com.socialstartup.mockenger.core.service.soap;

import com.socialstartup.mockenger.core.service.AbstractServiceTest;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.entity.PostEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by x079089 on 6/18/2015.
 */
public class PostServiceTest extends AbstractServiceTest {

    @InjectMocks
    private PostService classUnderTest;

    @Test
    public void testCreateMockRequest() throws IOException {
        PostEntity postEntity = classUnderTest.createMockRequest(GROUP_ID, SOAP_XML_BODY, httpServletRequestMock);
        checkEntityWithBody(postEntity, RequestType.POST, SOAP_XML_BODY);
    }

    @Test
    public void testCreateMockRequestWithSpaces() throws IOException {
        PostEntity postEntity = classUnderTest.createMockRequest(GROUP_ID, SOAP_XML_BODY, httpServletRequestMock);
        checkEntityWithBody(postEntity, RequestType.POST, SOAP_XML_BODY);
    }

    @Test
    public void testGetSoapBody() throws SOAPException, TransformerException, IOException {
        String soapBody = classUnderTest.getSoapBody(SOAP_XML_DATA, true);

        assertNotNull(soapBody);
        assertEquals(SOAP_XML_BODY, soapBody);
    }
}
