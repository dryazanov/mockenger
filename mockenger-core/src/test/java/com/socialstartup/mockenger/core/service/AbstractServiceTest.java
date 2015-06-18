package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
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
 * Created by x079089 on 6/18/2015.
 */
public abstract class AbstractServiceTest {

    protected static final String GROUP_ID = "GROUP12345";

    protected static final String JSON_DATA = "{\"valid\":\"ok\",\"mock\":\"4\"}";

    protected static final String JSON_WITH_SPACES = "{\"valid\": \"ok\",   \"mock\" : \"4\"}";

    protected static final String SOAP_XML_DATA = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>")
            .append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<S:Header/>")
            .append("<S:Body>")
            .append("<ns3:MyAccountCustomerDataRequestElement><identificationNumber>100000000002</identificationNumber></ns3:MyAccountCustomerDataRequestElement></S:Body>")
            .append("</S:Envelope>")
            .toString();

    protected static final String SOAP_XML_BODY = "<S:Body><ns3:MyAccountCustomerDataRequestElement><identificationNumber>100000000002</identificationNumber></ns3:MyAccountCustomerDataRequestElement></S:Body>";

    protected static final String SOAP_XML_BODY_WITH_SPACES = "<S:Body> <ns3:MyAccountCustomerDataRequestElement> <identificationNumber>100000000002</identificationNumber> </ns3:MyAccountCustomerDataRequestElement> </S:Body>";

    @Mock
    protected HttpServletRequest httpServletRequestMock;


    @Before
    public void init() {
        initMocks(this);

        Enumeration emptyEnumeration = Collections.enumeration(Collections.EMPTY_LIST);

        when(httpServletRequestMock.getAttribute(anyString())).thenReturn("");
        when(httpServletRequestMock.getHeaderNames()).thenReturn(emptyEnumeration);
        when(httpServletRequestMock.getParameterNames()).thenReturn(emptyEnumeration);
    }

    protected void checkEntityWithoutBody(RequestEntity entity, RequestType method) {
        String checkSum = CommonUtils.getCheckSum(entity);

        assertNotNull(entity);
        assertNull(entity.getBody());
        assertEquals(method, entity.getMethod());
        assertEquals(GROUP_ID, entity.getGroupId());
        assertEquals(checkSum, entity.getCheckSum());
    }

    protected void checkEntityWithBody(RequestEntity entity, RequestType method, String compareWith) {
        String checkSum = CommonUtils.getCheckSum(entity);

        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertEquals(compareWith, entity.getBody().getValue());
        assertEquals(method, entity.getMethod());
        assertEquals(GROUP_ID, entity.getGroupId());
        assertEquals(checkSum, entity.getCheckSum());
    }
}
