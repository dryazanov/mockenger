package com.socialstartup.mockenger.data.model.mock.response;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public interface IMockResponse {

    int getHttpStatus();

    void setHttpStatus(int httpStatus);

    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> responseHeaders);

    String getBody();

    void setBody(String responseBody);
}
