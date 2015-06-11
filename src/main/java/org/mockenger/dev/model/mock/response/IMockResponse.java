package org.mockenger.dev.model.mock.response;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public interface IMockResponse {

    HttpStatus getHttpStatus();

    void setHttpStatus(HttpStatus httpStatus);

    Map<String, String> getResponseHeaders();

    void setResponseHeaders(Map<String, String> responseHeaders);

    String getResponseBody();

    void setResponseBody(String responseBody);
}
