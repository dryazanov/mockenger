package com.socialstartup.mockenger.core.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.XPathTransformer;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class RequestServiceTest {

    private final static String GROUP_ID = "12345QWERTY";

    private static final String JSON1 = "{\"valid\":\"ok\",\"mock\":\"4\"}";
    private static final String JSON2 = "{\"valid\":\"not ok\",\"mock\":\"1\"}";

    private static final String URL1 = "/service/test/1";
    private static final String URL2 = "/service/test/2";

    private static final String XML_BODY1 = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading></note>";
    private static final String XML_BODY2 = "<note><to>Tove</to><from>Jani</from><heading>Huyainder</heading></note>";

    private static final String CHARSET_KOI8R = "charset=koi8r";
    private static final String CHARSET_UTF8 = "charset=utf-8";
    private static final String CONTENT_TYPE_VALUE1 = "application/soap+xml;" + CHARSET_UTF8;
    private static final String CONTENT_TYPE_VALUE2 = "text/jpeg; " + CHARSET_KOI8R;
    private static final String CONTENT_TYPE_VALUE3 = "application/soap+xml;" + CHARSET_KOI8R;

    private static final String HOST_VALUE1 = "localhost:8080";
    private static final String HOST_VALUE2 = "google.com";

    private static final String ACCEPT_VALUE1 = "*/*";

    private final Set<Pair> goodHeaders = new HashSet<>();
    private final Set<Pair> badHeaders = new HashSet<>();
    private final Set<Pair> moreHeaders = new HashSet<>();

    private static final String PARAM_NAME1 = "a";
    private static final String PARAM_NAME2 = "b";
    private static final String PARAM_NAME3 = "c";
    private static final String PARAM_VALUE1 = "1";
    private static final String PARAM_VALUE2 = "2";
    private static final String PARAM_VALUE3 = "3";
    private static final String PARAM_VALUE4 = "111";

    private final Set<Pair> goodParameters = new HashSet<>();
    private final Set<Pair> badParameters = new HashSet<>();
    private final Set<Pair> moreParameters = new HashSet<>();

    private final PostRequest postTestRequest = new PostRequest();
    private final PostRequest postRequest1 = new PostRequest();
    private final PostRequest postRequest2 = new PostRequest();
    private List<AbstractRequest> postRequestList;

    private final GetRequest getTestRequest = new GetRequest();
    private final GetRequest getRequest1 = new GetRequest();
    private final GetRequest getRequest2 = new GetRequest();
    private List<AbstractRequest> getRequestList;


    private final AbstractMapTransformer keyValueTransformerHeader = new KeyValueTransformer(HttpHeaders.CONTENT_TYPE, CHARSET_KOI8R, CHARSET_UTF8);
    private final AbstractMapTransformer keyValueTransformerParam = new KeyValueTransformer(PARAM_NAME1, PARAM_VALUE4, PARAM_VALUE1);
    private final AbstractTransformer regexpTransformerPath = new RegexpTransformer("\\d+", "1");
    private final AbstractTransformer regexpTransformerBody = new RegexpTransformer("(?<=<heading>)\\w+(?=</heading>)", "Reminder");
    private final AbstractTransformer xPathTransformerBody = new XPathTransformer("/note/heading/text()", "Reminder");

    @InjectMocks
    private RequestService classUnderTest;

    @Mock
    protected HttpServletRequest httpServletRequestMock;

    @Mock
    private RequestEntityRepository requestEntityRepositoryMock;


    @Before
    public void init() {
        initMocks(this);

        goodHeaders.add(new Pair(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE1));
        goodHeaders.add(new Pair(HttpHeaders.HOST, HOST_VALUE1));

        badHeaders.add(new Pair(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE2));
        badHeaders.add(new Pair(HttpHeaders.HOST, HOST_VALUE2));

        moreHeaders.add(new Pair(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE1));
        moreHeaders.add(new Pair(HttpHeaders.HOST, HOST_VALUE1));
        moreHeaders.add(new Pair(HttpHeaders.ACCEPT, ACCEPT_VALUE1));

        goodParameters.add(new Pair(PARAM_NAME1, PARAM_VALUE1));
        goodParameters.add(new Pair(PARAM_NAME2, PARAM_VALUE2));

        badParameters.add(new Pair(PARAM_NAME1, PARAM_VALUE2));
        badParameters.add(new Pair(PARAM_NAME2, PARAM_VALUE1));

        moreParameters.add(new Pair(PARAM_NAME1, PARAM_VALUE2));
        moreParameters.add(new Pair(PARAM_NAME2, PARAM_VALUE1));
        moreParameters.add(new Pair(PARAM_NAME3, PARAM_VALUE3));
    }

    @Test
    public void testDoFilterForPostPath() {
        createPostRequests();
        postRequest2.setPath(new Path(URL2));
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostPathWithTransformer() {
        createPostRequests();

        postTestRequest.setPath(new Path(URL2));
        postRequest1.setPath(new Path(ImmutableList.of(regexpTransformerPath), URL1));

        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostParameters() {
        createPostRequests();
        postRequest2.setParameters(new Parameters(badParameters));
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostParametersWithTransformer() {
        createPostRequests();
        Set<Pair> parameters = ImmutableSet.of(new Pair(PARAM_NAME2, PARAM_VALUE2), new Pair(PARAM_NAME1, PARAM_VALUE4));

        postTestRequest.setParameters(new Parameters(parameters));
        postRequest1.setParameters(new Parameters(ImmutableList.of(keyValueTransformerParam), goodParameters));

        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostHeaders() {
        createPostRequests();
        postRequest2.setHeaders(new Headers(badHeaders));
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostHeadersWithTransformer() {
        createPostRequests();
        Set<Pair> headers = ImmutableSet.of(new Pair(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE3), new Pair(HttpHeaders.HOST, HOST_VALUE1));

        postTestRequest.setHeaders(new Headers(headers));
        postRequest1.setHeaders(new Headers(ImmutableList.of(keyValueTransformerHeader), goodHeaders));

        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostMoreHeaders() {
        createPostRequests();
        postTestRequest.setHeaders(new Headers(moreHeaders));
        postRequest2.setHeaders(new Headers(badHeaders));
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostBody() {
        createPostRequests();
        postRequest2.setBody(new Body(JSON2));
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForGetBody() {
        createGetRequests();
        getRequest2.setPath(new Path(URL2));
        checkCorrectResult(classUnderTest.doFilter(getTestRequest, getRequestList), getRequest1);
    }

    @Test
    public void testDoFilterForPostBodyWithTransformer1() {
        createPostRequests();

        postTestRequest.setBody(new Body(XML_BODY2));
        postRequest1.setBody(new Body(ImmutableList.of(regexpTransformerBody), XML_BODY1));
        postRequest1.setCheckSum(CommonUtils.getCheckSum(postRequest1));

        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostBodyWithTransformer2() {
        createPostRequests();

        postTestRequest.setBody(new Body(XML_BODY2));
        postRequest1.setBody(new Body(ImmutableList.of(xPathTransformerBody), XML_BODY1));
        postRequest1.setCheckSum(CommonUtils.getCheckSum(postRequest1));

        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostWithTwoFoundMocks() {
        createPostRequests();
        checkCorrectResult(classUnderTest.doFilter(postTestRequest, postRequestList), postRequest1);
    }

    @Test
    public void testDoFilterForPostMoreParameters() {
        createPostRequests();
        postRequest1.setParameters(new Parameters(moreParameters));
        postRequest2.setParameters(new Parameters(badParameters));

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    @Test
    public void testDoFilterForPostBadPathNothingFound() {
        createPostRequests();
        Path path = new Path(URL2);
        postRequest1.setPath(path);
        postRequest2.setPath(path);

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    @Test
    public void testDoFilterForPostBadParametersNothingFound() {
        createPostRequests();
        Parameters parameters = new Parameters(badParameters);
        postRequest1.setParameters(parameters);
        postRequest2.setParameters(parameters);

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    @Test
    public void testDoFilterForPostBadHeadersNothingFound() {
        createPostRequests();
        Headers headers = new Headers(badHeaders);
        postRequest1.setHeaders(headers);
        postRequest2.setHeaders(headers);

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    @Test
    public void testDoFilterForPostMoreHeadersNothingFound() {
        createPostRequests();
        postRequest1.setHeaders(new Headers(moreHeaders));
        postRequest2.setHeaders(new Headers(badHeaders));

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    @Test
    public void testDoFilterForPostBadBodyNothingFound() {
        createPostRequests();
        Body body = new Body(JSON2);
        String checksum = CommonUtils.generateCheckSum(body.getValue());
        postRequest1.setBody(body);
        postRequest1.setCheckSum(checksum);
        postRequest2.setBody(body);
        postRequest2.setCheckSum(checksum);

        assertNull(classUnderTest.doFilter(postTestRequest, postRequestList));
    }

    private void createGetRequests() {
        // Simulate request from user
        getTestRequest.setGroupId(GROUP_ID);
        getTestRequest.setPath(new Path(URL1));
        getTestRequest.setHeaders(new Headers(goodHeaders));
        getTestRequest.setParameters(new Parameters(goodParameters));
        getTestRequest.setCheckSum(CommonUtils.getCheckSum(getTestRequest));

        // Initially "good" mocked entity
        getRequest1.setGroupId(GROUP_ID);
        getRequest1.setPath(new Path(URL1));
        getRequest1.setHeaders(new Headers(goodHeaders));
        getRequest1.setParameters(new Parameters(goodParameters));
        getRequest1.setCheckSum(CommonUtils.getCheckSum(getRequest1));

        // Initially "good" mocked entity but some parts will be converted to "bad"
        getRequest2.setGroupId(GROUP_ID);
        getRequest2.setPath(new Path(URL1));
        getRequest2.setHeaders(new Headers(goodHeaders));
        getRequest2.setParameters(new Parameters(goodParameters));
        getRequest2.setCheckSum(CommonUtils.getCheckSum(getRequest2));

        getRequestList = ImmutableList.of(getRequest2, getRequest1);
    }
    
    private void createPostRequests() {
        // Simulate request from user
        postTestRequest.setGroupId(GROUP_ID);
        postTestRequest.setPath(new Path(URL1));
        postTestRequest.setHeaders(new Headers(goodHeaders));
        postTestRequest.setParameters(new Parameters(goodParameters));
        postTestRequest.setBody(new Body(JSON1));
        postTestRequest.setCheckSum(CommonUtils.getCheckSum(postTestRequest));

        // Initially "good" mocked entity
        postRequest1.setGroupId(GROUP_ID);
        postRequest1.setPath(new Path(URL1));
        postRequest1.setHeaders(new Headers(goodHeaders));
        postRequest1.setParameters(new Parameters(goodParameters));
        postRequest1.setBody(new Body(JSON1));
        postRequest1.setCheckSum(CommonUtils.getCheckSum(postRequest1));
        postRequest1.setMockResponse(new MockResponse());

        // Initially "good" mocked entity but some parts will be converted to "bad"
        postRequest2.setGroupId(GROUP_ID);
        postRequest2.setPath(new Path(URL1));
        postRequest2.setHeaders(new Headers(goodHeaders));
        postRequest2.setParameters(new Parameters(goodParameters));
        postRequest2.setBody(new Body(JSON1));
        postRequest2.setCheckSum(CommonUtils.getCheckSum(postRequest2));
        postRequest2.setMockResponse(new MockResponse());

        postRequestList = ImmutableList.of(postRequest2, postRequest1);
    }

    private void checkCorrectResult(AbstractRequest result, AbstractRequest request) {
        assertNotNull(result);
        assertEquals(request.getGroupId(), result.getGroupId());
        assertEquals(request.getCheckSum(), result.getCheckSum());
        assertEquals(request.getPath().getValue(), result.getPath().getValue());
        assertEquals(request.getHeaders().getValues(), result.getHeaders().getValues());
        assertEquals(request.getParameters().getValues(), result.getParameters().getValues());
        assertEquals(request.getCheckSum(), result.getCheckSum());
    }

    @Test
    public void testFindMockedEntitiesNullResult() {
        createPostRequests();
        when(requestEntityRepositoryMock.findByGroupIdAndMethod(eq(GROUP_ID), eq(RequestMethod.POST))).thenReturn(Collections.emptyList());
        AbstractRequest result = classUnderTest.findMockedEntities(postTestRequest);

        assertNull(result);
        verify(requestEntityRepositoryMock).findByGroupIdAndMethod(eq(GROUP_ID), eq(RequestMethod.POST));
        verifyNoMoreInteractions(requestEntityRepositoryMock);
    }

    @Test
    public void testFindMockedEntitiesNotNullResult() {
        createPostRequests();
        when(requestEntityRepositoryMock.findByGroupIdAndMethod(eq(GROUP_ID), eq(RequestMethod.POST))).thenReturn(postRequestList);
        AbstractRequest result = classUnderTest.findMockedEntities(postTestRequest);

        assertNotNull(result);
        assertNotNull(result.getMockResponse());
        verify(requestEntityRepositoryMock).findByGroupIdAndMethod(eq(GROUP_ID), eq(RequestMethod.POST));
        verifyNoMoreInteractions(requestEntityRepositoryMock);
    }
}
