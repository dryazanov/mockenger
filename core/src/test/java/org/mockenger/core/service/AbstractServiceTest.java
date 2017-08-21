package org.mockenger.core.service;

import org.mockenger.core.util.CommonUtils;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.junit.Before;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

import static java.util.Collections.EMPTY_LIST;
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

    protected static final String XML_DATA = "<note>" +
			"<to>Tove</to>" +
			"<from>Jani</from>" +
			"<heading>Reminder</heading>" +
			"<body>Don't forget me this weekend!</body>" +
			"</note>";

    protected static final String XML_DATA_WITH_XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<note>" +
			"<to>Tove</to>" +
			"<from>Jani</from>" +
			"<heading>Reminder</heading>" +
			"<body>Don't forget me this weekend!</body>" +
			"</note>";

    protected static final String XML_DATA_WITH_SPACES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
			" <note> " +
			"<to>Tove</to> " +
			" <from>Jani</from>" +
			"<heading>Reminder</heading> " +
			" <body>Don't forget me this weekend!</body> " +
			"</note> ";

    protected static final String SOAP_XML_BODY = "<S:Body xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<ns3:MyAccountCustomerDataRequestElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">" +
			"<identificationNumber>100000000002</identificationNumber>" +
			"</ns3:MyAccountCustomerDataRequestElement>" +
			"</S:Body>";

    protected static final String SOAP_XML_DATA = "<?xml version='1.0' encoding='UTF-8'?>" +
			"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<S:Header/>" +
			SOAP_XML_BODY +
			"</S:Envelope>";

    @Mock
    protected HttpServletRequest httpServletRequestMock;


    @Before
    public void init() {
        initMocks(this);

        final Enumeration<String> emptyEnumeration = Collections.enumeration(EMPTY_LIST);

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

    protected void checkEntityWithBody(final GenericRequest entity, final RequestMethod method, final String compareWith) {
        final String checkSum = CommonUtils.getCheckSum(entity);

        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertEquals(compareWith, entity.getBody().getValue());
        assertEquals(method, entity.getMethod());
        assertEquals(GROUP_ID, entity.getGroupId());
        assertEquals(checkSum, entity.getCheckSum());
    }
}
