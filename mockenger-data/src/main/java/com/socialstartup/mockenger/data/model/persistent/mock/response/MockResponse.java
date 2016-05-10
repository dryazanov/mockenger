package com.socialstartup.mockenger.data.model.persistent.mock.response;

import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import lombok.ToString;

import java.util.Set;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@ToString
public class MockResponse {

    private int httpStatus;

    private Set<Pair> headers;

    private String body;


    public MockResponse() {
        // default constructor
    }

    public MockResponse(int httpStatus, Set<Pair> headers, String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Set<Pair> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Pair> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
