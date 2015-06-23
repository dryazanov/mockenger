package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.mock.request.entity.PostEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.mock.request.part.Path;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by x079089 on 6/18/2015.
 */
public class RequestServiceTest {

    private static String GROUP_ID = "12345QWERTY";

    private static final String JSON1 = "{\"valid\":\"ok\",\"mock\":\"4\"}";
    private static final String JSON2 = "{\"valid\":\"not ok\",\"mock\":\"1\"}";

    private static String URL1 = "/service/test/1";
    private static String URL2 = "/service/test/2";

    private static String CONTENT_TYPE = "content-type";
    private static String CONTENT_TYPE_VALUE1 = "application/soap+xml;charset=utf-8";
    private static String CONTENT_TYPE_VALUE2 = "text/jpeg; charset=koi8r";

    private static String HOST = "host";
    private static String HOST_VALUE1 = "localhost:8080";
    private static String HOST_VALUE2 = "google.com";

    private static String ACCEPT = "accept";
    private static String ACCEPT_VALUE1 = "*/*";

    private Map<String, String> goodHeaders = new HashMap<>();
    private Map<String, String> badHeaders = new HashMap<>();
    private Map<String, String> moreHeaders = new HashMap<>();

    private static String PARAM_NAME1 = "a";
    private static String PARAM_NAME2 = "b";
    private static String PARAM_NAME3 = "c";
    private static String PARAM_VALUE1 = "1";
    private static String PARAM_VALUE2 = "2";
    private static String PARAM_VALUE3 = "3";

    private Map<String, String> goodParameters = new TreeMap<>();
    private Map<String, String> badParameters = new TreeMap<>();
    private Map<String, String> moreParameters = new TreeMap<>();


    private PostEntity entityUnderTest = new PostEntity();
    private PostEntity postEntity1 = new PostEntity();
    private PostEntity postEntity2 = new PostEntity();
    private List<RequestEntity> entityList;

    @InjectMocks
    private RequestService classUnderTest;

    @Mock
    protected HttpServletRequest httpServletRequestMock;

    @Before
    public void init() {
        initMocks(this);

        goodHeaders.put(CONTENT_TYPE, CONTENT_TYPE_VALUE1);
        goodHeaders.put(HOST, HOST_VALUE1);

        badHeaders.put(CONTENT_TYPE, CONTENT_TYPE_VALUE2);
        badHeaders.put(HOST, HOST_VALUE2);

        moreHeaders.put(CONTENT_TYPE, CONTENT_TYPE_VALUE1);
        moreHeaders.put(HOST, HOST_VALUE1);
        moreHeaders.put(ACCEPT, ACCEPT_VALUE1);

        goodParameters.put(PARAM_NAME1, PARAM_VALUE1);
        goodParameters.put(PARAM_NAME2, PARAM_VALUE2);

        badParameters.put(PARAM_NAME1, PARAM_VALUE2);
        badParameters.put(PARAM_NAME2, PARAM_VALUE1);

        moreParameters.put(PARAM_NAME1, PARAM_VALUE2);
        moreParameters.put(PARAM_NAME2, PARAM_VALUE1);
        moreParameters.put(PARAM_NAME3, PARAM_VALUE3);

        entityUnderTest.setGroupId(GROUP_ID);
        entityUnderTest.setPath(new Path(URL1));
        entityUnderTest.setHeaders(new Headers(goodHeaders));
        entityUnderTest.setParameters(new Parameters(goodParameters));
        entityUnderTest.setBody(new Body(JSON1));
        entityUnderTest.setCheckSum(CommonUtils.getCheckSum(entityUnderTest));

        postEntity1.setGroupId(GROUP_ID);
        postEntity1.setPath(new Path(URL1));
        postEntity1.setHeaders(new Headers(goodHeaders));
        postEntity1.setParameters(new Parameters(goodParameters));
        postEntity1.setBody(new Body(JSON1));
        postEntity1.setCheckSum(CommonUtils.getCheckSum(postEntity1));

        postEntity2.setGroupId(GROUP_ID);
        postEntity2.setPath(new Path(URL1));
        postEntity2.setHeaders(new Headers(goodHeaders));
        postEntity2.setParameters(new Parameters(goodParameters));
        postEntity2.setBody(new Body(JSON1));
        postEntity2.setCheckSum(CommonUtils.getCheckSum(postEntity2));

        entityList = new ArrayList<>(Arrays.asList(postEntity1, postEntity2));

//        Enumeration headers = Collections.enumeration(Arrays.asList(CONTENT_TYPE, HOST));
//        when(httpServletRequestMock.getHeader(eq(CONTENT_TYPE))).thenReturn("");
//        when(httpServletRequestMock.getHeader(eq(HOST))).thenReturn("");
//        when(httpServletRequestMock.getHeaderNames()).thenReturn(headers);

//        Enumeration emptyEnumeration = Collections.enumeration(Collections.EMPTY_LIST);

//        when(httpServletRequestMock.getAttribute(anyString())).thenReturn("");
//        when(httpServletRequestMock.getParameterNames()).thenReturn(emptyEnumeration);

    }

    @Test
    public void testDoFilterForPostBadPath() {
        postEntity2.setPath(new Path(URL2));
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadParameters() {
        postEntity2.setParameters(new Parameters(badParameters));
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadHeaders() {
        postEntity2.setHeaders(new Headers(badHeaders));
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostMoreHeaders() {
        entityUnderTest.setHeaders(new Headers(moreHeaders));
        postEntity2.setHeaders(new Headers(badHeaders));
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadBody() {
        postEntity2.setBody(new Body(JSON2));
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostWithTwoFoundMocks() {
        checkCorrectResult(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostMoreParameters() {
        postEntity1.setParameters(new Parameters(moreParameters));
        postEntity2.setParameters(new Parameters(badParameters));

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadPathNothingFound() {
        Path path = new Path(URL2);
        postEntity1.setPath(path);
        postEntity2.setPath(path);

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadParametersNothingFound() {
        Parameters parameters = new Parameters(badParameters);
        postEntity1.setParameters(parameters);
        postEntity2.setParameters(parameters);

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadHeadersNothingFound() {
        Headers headers = new Headers(badHeaders);
        postEntity1.setHeaders(headers);
        postEntity2.setHeaders(headers);

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostMoreHeadersNothingFound() {
        postEntity1.setHeaders(new Headers(moreHeaders));
        postEntity2.setHeaders(new Headers(badHeaders));

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    @Test
    public void testDoFilterForPostBadBodyNothingFound() {
        Body body = new Body(JSON2);
        String checksum = CommonUtils.generateCheckSum(body.getValue());
        postEntity1.setBody(body);
        postEntity1.setCheckSum(checksum);
        postEntity2.setBody(body);
        postEntity2.setCheckSum(checksum);

        assertNull(classUnderTest.doFilter(entityUnderTest, entityList));
    }

    private void checkCorrectResult(RequestEntity result) {
        assertNotNull(result);
        assertEquals(postEntity1.getGroupId(), result.getGroupId());
        assertEquals(postEntity1.getCheckSum(), result.getCheckSum());
        assertEquals(postEntity1.getPath().getValue(), result.getPath().getValue());
        assertEquals(postEntity1.getHeaders().getValues(), result.getHeaders().getValues());
        assertEquals(postEntity1.getParameters().getValues(), result.getParameters().getValues());
        assertEquals(postEntity1.getCheckSum(), result.getCheckSum());
    }
}
