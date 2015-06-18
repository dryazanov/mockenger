package com.socialstartup.mockenger.data.model.mock.request.entity;

import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PostEntity extends RequestEntity {
    public PostEntity() {
        setMethod(RequestType.POST);
    }

    public PostEntity(Body body) {
        this();
        setBody(body);
    }
}
