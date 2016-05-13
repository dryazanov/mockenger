package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.junit.Before;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public abstract class AbstractServiceTest {

    protected static final String GROUP_ID = "GROUP12345";

    protected static final String JSON_DATA = "{\"valid\":\"ok\",\"mock\":\"4\"}";

    protected static final String JSON_WITH_SPACES = "{\"valid\": \"ok\",   \"mock\" : \"4\"}";

    protected static final String XML_DATA = new StringBuilder()
            .append("<note>")
            .append("<to>Tove</to>")
            .append("<from>Jani</from>")
            .append("<heading>Reminder</heading>")
            .append("<body>Don't forget me this weekend!</body>")
            .append("</note>")
            .toString();

    protected static final String XML_DATA_WITH_XML_DECLARATION = new StringBuilder()
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<note>")
            .append("<to>Tove</to>")
            .append("<from>Jani</from>")
            .append("<heading>Reminder</heading>")
            .append("<body>Don't forget me this weekend!</body>")
            .append("</note>")
            .toString();

    protected static final String XML_DATA_WITH_SPACES = new StringBuilder()
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ")
            .append(" <note> ")
            .append("<to>Tove</to> ")
            .append(" <from>Jani</from>")
            .append("<heading>Reminder</heading> ")
            .append(" <body>Don't forget me this weekend!</body> ")
            .append("</note> ")
            .toString();

    protected static final String SOAP_XML_BODY = new StringBuilder()
            .append("<S:Body xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<ns3:MyAccountCustomerDataRequestElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">")
            .append("<identificationNumber>100000000002</identificationNumber>")
            .append("</ns3:MyAccountCustomerDataRequestElement>")
            .append("</S:Body>")
            .toString();

    protected static final String SOAP_XML_DATA = new StringBuilder()
            .append("<?xml version='1.0' encoding='UTF-8'?>")
            .append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<S:Header/>")
            .append(SOAP_XML_BODY)
            .append("</S:Envelope>")
            .toString();

    @Mock
    protected HttpServletRequest httpServletRequestMock;


    @Before
    public void init() {
        initMocks(this);

        final Enumeration<String> emptyEnumeration = Collections.enumeration(Collections.EMPTY_LIST);

        when(httpServletRequestMock.getAttribute(anyString())).thenReturn("");
        when(httpServletRequestMock.getHeaderNames()).thenReturn(emptyEnumeration);
        when(httpServletRequestMock.getParameterNames()).thenReturn(emptyEnumeration);
    }

    protected void checkEntityWithoutBody(final AbstractRequest entity, final RequestMethod method) {
        final String checkSum = CommonUtils.getCheckSum(entity);

        assertNotNull(entity);
        assertNull(entity.getBody());
        assertEquals(method, entity.getMethod());
        assertEquals(GROUP_ID, entity.getGroupId());
        assertEquals(checkSum, entity.getCheckSum());
    }

    protected void checkEntityWithBody(final AbstractRequest entity, final RequestMethod method, final String compareWith) {
        final String checkSum = CommonUtils.getCheckSum(entity);

        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertEquals(compareWith, entity.getBody().getValue());
        assertEquals(method, entity.getMethod());
        assertEquals(GROUP_ID, entity.getGroupId());
        assertEquals(checkSum, entity.getCheckSum());
    }
}
