package com.socialstartup.mockenger.data.model.mock.request.entity;

import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PatchEntity extends RequestEntity {
    public PatchEntity() {
        setMethod(RequestType.PATCH);
    }

    public PatchEntity(Body body) {
        this();
        setBody(body);
    }
}
