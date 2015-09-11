package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.config.MockengerHeadersStopListConfigParam;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by Dmitry Ryazanov on 11-Sep-15.
 */
@Component
public class HttpHeadersService {

    public static final String CONTENT_TYPE_KEY = "content-type";
    public static final String CHARSET_UTF_8 = "charset=UTF-8";
    public static final String MEDIA_TYPE_JSON = String.format("%s;%s", MediaType.APPLICATION_JSON_VALUE, CHARSET_UTF_8);
    public static final String MEDIA_TYPE_XML = String.format("%s;%s", MediaType.APPLICATION_XML_VALUE, CHARSET_UTF_8);

    @Autowired
    private MockengerHeadersStopListConfigParam stopListConfigParam;

    /**
     * @return
     */
    public HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();

        // Default content-type
        headers.set(CONTENT_TYPE_KEY, MEDIA_TYPE_JSON);
        return headers;
    }

    /**
     * @param headerList
     * @return
     */
    public HttpHeaders createHeaders(Collection<Pair> headerList) {
        HttpHeaders headers = getDefaultHeaders();

        for (Pair pair : headerList) {
            if (!stopListConfigParam.getResponseHeaders().contains(pair.getKey().toLowerCase())) {
                headers.set(pair.getKey(), pair.getValue());
            }
        }

        return headers;
    }
}
