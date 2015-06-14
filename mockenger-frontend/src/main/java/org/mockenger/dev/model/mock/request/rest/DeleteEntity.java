package org.mockenger.dev.model.mock.request.rest;

import org.mockenger.dev.model.mock.request.RequestEntity;
import org.mockenger.dev.model.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class DeleteEntity extends RequestEntity {
    public DeleteEntity() {
        setMethod(RequestMethod.DELETE);
    }

    @Override
    public Body getBody() {
        throw new NotImplementedException();
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
