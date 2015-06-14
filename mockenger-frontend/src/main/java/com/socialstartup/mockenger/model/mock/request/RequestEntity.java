package com.socialstartup.mockenger.model.mock.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import com.socialstartup.mockenger.model.mock.response.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * Created by x079089 on 3/12/2015.
 */
public class RequestEntity implements IRequestEntity {

    /**
     * Logger
     */
    @JsonIgnore
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMockRequest.class);


    @Id
    private String id;

    private String groupId;

    private String name;

    private Date creationDate;

    private RequestMethod method;

    private Path path;

    private Headers headers;

    private Parameters parameters;

    private Body body;

    private String checkSum;

    private MockResponse response;

//    private List<ITransformer> transformers;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public RequestMethod getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public String getCheckSum() {
        return checkSum;
    }

    @Override
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    @Override
    public MockResponse getResponse() {
        return response;
    }

    @Override
    public void setResponse(MockResponse response) {
        this.response = response;
    }

    /*@Override
    public List<ITransformer> getTransformers() {
        return transformers;
    }

    @Override
    public void setTransformers(List<ITransformer> transformers) {
        this.transformers = transformers;
    }*/


    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOG.error("Couldn't convert object to json string", e);
        }

        return this.toString();
    }
}
