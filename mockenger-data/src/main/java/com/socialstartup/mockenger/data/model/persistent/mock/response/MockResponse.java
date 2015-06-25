package com.socialstartup.mockenger.data.model.persistent.mock.response;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public class MockResponse {

    private int httpStatus;

    private Map<String, String> headers;

    private String body;


    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
