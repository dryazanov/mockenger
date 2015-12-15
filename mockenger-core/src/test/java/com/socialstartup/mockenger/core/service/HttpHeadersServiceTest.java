package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Dmitry Ryazanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestPropertyContext.class)
public class HttpHeadersServiceTest {

    private static final List<String> HEADERS_TO_ADD = Arrays.asList(MediaType.APPLICATION_JSON_VALUE, "host");

    @InjectMocks
    private final HttpHeadersService classUnderTest = new HttpHeadersService();

    @Value("#{'${mockenger.mock.response.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    @Before
    public void init() {
        ReflectionTestUtils.setField(classUnderTest, "headersToIgnore", headersToIgnore);
    }

    @Test
    public void testGetDefaultHeaders() {
        HttpHeaders headers = classUnderTest.getDefaultHeaders();
        assertEquals(HttpHeadersService.MEDIA_TYPE_JSON, headers.get(HttpHeadersService.CONTENT_TYPE_KEY).get(0));
    }

    @Test
    public void testCreateHeaders() {
        List<Pair> pairList = new ArrayList<>(3);
        HEADERS_TO_ADD.forEach( item -> pairList.add(new Pair(item, "value")) );
        headersToIgnore.forEach( item -> pairList.add(new Pair(item, "value")) );

        HttpHeaders headers = classUnderTest.createHeaders(pairList);
        assertEquals(3, headers.size());
        headersToIgnore.forEach( header -> assertNull(headers.get(header)) );
    }
}
