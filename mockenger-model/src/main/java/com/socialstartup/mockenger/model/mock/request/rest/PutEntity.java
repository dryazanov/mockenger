package com.socialstartup.mockenger.model.mock.request.rest;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class PutEntity extends RequestEntity {
    public PutEntity() {
        setMethod(RequestType.PUT);
    }

    @Override
    public String getCheckSum() {
        return DigestUtils.md5DigestAsHex(getBody().getValue().getBytes());
    }
}
