package com.socialstartup.mockenger.data.model.mock.request;

import com.socialstartup.mockenger.data.model.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.mock.response.MockResponse;

import java.util.Date;

/**
 * Created by x079089 on 3/29/2015.
 */
public interface IRequestEntity extends IMockRequest {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    Date getCreationDate();

    void setCreationDate(Date creationDate);

    Path getPath();

    void setPath(Path path);

    Headers getHeaders();

    void setHeaders(Headers headers);

    Parameters getParameters();

    void setParameters(Parameters parameters);

    Body getBody();

    void setBody(Body body);

    String getCheckSum();

    void setCheckSum(String checkSum);

    MockResponse getResponse();

    void setResponse(MockResponse response);
}
