package com.socialstartup.mockenger.model.mock.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by x079089 on 3/12/2015.
 */
public abstract class AbstractMockRequest implements IMockRequest {

    /**
     * Logger
     */
    @JsonIgnore
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMockRequest.class);

    private String groupId;

    private RequestMethod method;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
