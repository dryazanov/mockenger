package com.socialstartup.mockenger.model.mock.request.entity;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PostEntity extends RequestEntity {
    public PostEntity() {
        setMethod(RequestType.POST);
    }
}
