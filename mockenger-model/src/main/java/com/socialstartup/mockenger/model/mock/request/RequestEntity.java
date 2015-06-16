package com.socialstartup.mockenger.model.mock.request;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import com.socialstartup.mockenger.model.mock.response.MockResponse;
import com.socialstartup.mockenger.model.persistent.base.AbstractPersistentEntity;

import java.util.Date;

/**
 * Created by x079089 on 3/12/2015.
 */
public class RequestEntity extends AbstractPersistentEntity<String> {

    private String groupId;

    private String name;

    private Date creationDate;

    private RequestType method;

    private Path path;

    private Headers headers;

    private Parameters parameters;

    protected Body body;

    private String checkSum;

    private MockResponse mockResponse;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public RequestType getMethod() {
        return this.method;
    }

    public void setMethod(RequestType method) {
        this.method = method;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public MockResponse getMockResponse() {
        return mockResponse;
    }

    public void setMockResponse(MockResponse mockResponse) {
        this.mockResponse = mockResponse;
    }
}
