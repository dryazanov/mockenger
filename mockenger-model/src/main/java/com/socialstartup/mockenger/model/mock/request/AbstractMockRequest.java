package com.socialstartup.mockenger.model.mock.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.model.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

/**
 * Created by x079089 on 3/12/2015.
 */
public abstract class AbstractMockRequest implements IMockRequest {

    /**
     * Logger
     */
    @Transient
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMockRequest.class);

    private String groupId;

    private RequestType method;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public RequestType getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(RequestType method) {
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
