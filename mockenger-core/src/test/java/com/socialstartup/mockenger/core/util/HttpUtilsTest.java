package com.socialstartup.mockenger.core.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class HttpUtilsTest {

    private final static String CONTENT_TYPE = "content-type";
    private final static String HOST = "host";
    private final static String ACCEPT = "accept";

    private static final String CONTENT_TYPE_VALUE1 = "application/SOAP+XML; charset=UTF-8";
    private static final String CONTENT_TYPE_VALUE2 = "application/soap+xml;charset=utf-8";
    private static final String CONTENT_TYPE_VALUE3 = "application/SOAP+XML;charset=UTF-8";
    private static final String HOST_VALUE1 = "LOCALHOST:8080";
    private static final String HOST_VALUE2 = "localhost:8080";
    private static final String ACCEPT_VALUE = "*/*";

    private static final String PARAM_NAME1 = "a";
    private static final String PARAM_NAME2 = "b";
    private static final String PARAM_NAME3 = "c";
    private static final String PARAM_VALUE1 = "1";
    private static final String PARAM_VALUE2 = "2";
    private static final String PARAM_VALUE3 = "3";
    private static final String PARAM_VALUE4 = "111";

    @Mock
    protected HttpServletRequest httpServletRequestMock;


    @Before
    public void init() {
        initMocks(this);

        Enumeration headerNames = Collections.enumeration(Arrays.asList(CONTENT_TYPE, HOST, ACCEPT));
        when(httpServletRequestMock.getHeaderNames()).thenReturn(headerNames);
        when(httpServletRequestMock.getHeader(CONTENT_TYPE)).thenReturn(CONTENT_TYPE_VALUE1);
        when(httpServletRequestMock.getHeader(HOST)).thenReturn(HOST_VALUE1);
        when(httpServletRequestMock.getHeader(ACCEPT)).thenReturn(ACCEPT_VALUE);


        Enumeration params = Collections.enumeration(Arrays.asList(PARAM_NAME1, PARAM_NAME2, PARAM_NAME3));
        when(httpServletRequestMock.getParameterNames()).thenReturn(params);
        when(httpServletRequestMock.getParameter(PARAM_NAME1)).thenReturn(PARAM_VALUE1);
        when(httpServletRequestMock.getParameter(PARAM_NAME2)).thenReturn(PARAM_VALUE2);
        when(httpServletRequestMock.getParameter(PARAM_NAME3)).thenReturn(PARAM_VALUE3);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> resultMap = HttpUtils.getHeaders(httpServletRequestMock, false);
        assertEquals(3, resultMap.size());
        assertEquals(CONTENT_TYPE_VALUE2, resultMap.get(CONTENT_TYPE));
        assertEquals(HOST_VALUE2, resultMap.get(HOST));
        assertEquals(ACCEPT_VALUE, resultMap.get(ACCEPT));
    }

    @Test
    public void testGetHeadersStrict() {
        Map<String, String> resultMap = HttpUtils.getHeaders(httpServletRequestMock, true);
        assertEquals(3, resultMap.size());
        assertEquals(CONTENT_TYPE_VALUE3, resultMap.get(CONTENT_TYPE));
        assertEquals(HOST_VALUE1, resultMap.get(HOST));
        assertEquals(ACCEPT_VALUE, resultMap.get(ACCEPT));
    }

    @Test
    public void testGetParameterMap() {
        Map<String, String> resultMap = HttpUtils.getParameterMap(httpServletRequestMock);
        assertEquals(3, resultMap.size());
        assertEquals(PARAM_VALUE1, resultMap.get(PARAM_NAME1));
        assertEquals(PARAM_VALUE2, resultMap.get(PARAM_NAME2));
        assertEquals(PARAM_VALUE3, resultMap.get(PARAM_NAME3));
    }
}
