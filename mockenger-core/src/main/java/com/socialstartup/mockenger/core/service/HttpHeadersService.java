package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitry Ryazanov on 11-Sep-15.
 */
@Component
public class HttpHeadersService {

    public static final String CONTENT_TYPE_KEY = HttpHeaders.CONTENT_TYPE.toLowerCase();
    public static final String CHARSET_UTF_8 = "charset=UTF-8";
    public static final String MEDIA_TYPE_JSON = String.format("%s;%s", MediaType.APPLICATION_JSON_VALUE, CHARSET_UTF_8);
    public static final String MEDIA_TYPE_XML = String.format("%s;%s", MediaType.APPLICATION_XML_VALUE, CHARSET_UTF_8);

    @Value("#{'${mockenger.mock.response.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    /**
     * @return object HttpHeaders with default headers inside
     */
    public HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();

        // Default content-type
        headers.set(HttpHeaders.CONTENT_TYPE, MEDIA_TYPE_JSON);
        return headers;
    }

    /**
     * Creates header list using headersToIgnore as a filter
     *
     * @param headerList collection of headers to be added
     * @return object HttpHeaders with added headers
     */
    public HttpHeaders createHeaders(Collection<Pair> headerList) {
        HttpHeaders headers = getDefaultHeaders();

        headerList.forEach(pair -> {
            if (!headersToIgnore.contains(pair.getKey().toLowerCase())) {
                headers.set(pair.getKey(), pair.getValue());
            }
        });

        return headers;
    }
}
