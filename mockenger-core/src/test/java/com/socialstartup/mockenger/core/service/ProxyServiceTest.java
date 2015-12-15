package com.socialstartup.mockenger.core.service;

import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Dmitry Ryazanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestPropertyContext.class)
public class ProxyServiceTest {
    private static final String BASE_URL = "http://httpbin.org";
    private static final String REQUEST_PATH = "/get";
    private static final String MOCK_RESPONSE_BODY = "{\"result\":\"OK\"}";

    @InjectMocks
    private final ProxyService classUnderTest = new ProxyService();

    @Value("#{'${mockenger.proxy.request.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    @Before
    public void init() {
        ReflectionTestUtils.setField(classUnderTest, "headersToIgnore", headersToIgnore);
    }

    @Test
    public void testForwardRequest() {
        AbstractRequest abstractRequest = classUnderTest.forwardRequest(createNewRequest(), BASE_URL);
        assertNotNull(abstractRequest);
        assertNotEquals(MOCK_RESPONSE_BODY, abstractRequest.getMockResponse().getBody());
    }

    protected static AbstractRequest createNewRequest() {
        AbstractRequest request = new GetRequest();
        Pair contentType = new Pair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Pair host = new Pair(HttpHeaders.HOST, "localhost");
        Set<Pair> headersSet = Sets.newSet(contentType, host);

        request.setCreationDate(new Date());
        request.setPath(new Path(REQUEST_PATH));
        request.setParameters(new Parameters(ImmutableSet.of(new Pair("A", "1"), new Pair("b", "2"))));
        request.setHeaders(new Headers(headersSet));
        request.setMockResponse(new MockResponse(200, headersSet, MOCK_RESPONSE_BODY));

        return request;
    }
}
