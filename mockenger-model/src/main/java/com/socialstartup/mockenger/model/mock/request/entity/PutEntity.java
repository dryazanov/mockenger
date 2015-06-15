package com.socialstartup.mockenger.model.mock.request.entity;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PutEntity extends RequestEntity {
    public PutEntity() {
        setMethod(RequestType.PUT);
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
    }
}
