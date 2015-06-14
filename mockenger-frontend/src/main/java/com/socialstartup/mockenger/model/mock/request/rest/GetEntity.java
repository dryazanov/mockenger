package com.socialstartup.mockenger.model.mock.request.rest;

import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class GetEntity extends RequestEntity {

    public GetEntity() {
        setMethod(RequestMethod.GET);
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public void setBody(Body body) {
        throw new NotImplementedException();
    }

    @Override
    public String getCheckSum() {
        String str = this.getPath().getValue() + getParameters().getValues() + getMethod();
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}
