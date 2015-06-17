package com.socialstartup.mockenger.data.model.mock.request.entity;

import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by x079089 on 6/15/2015.
 */
public abstract class BodilessEntity extends RequestEntity {
    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public void setBody(Body body) {
        throw new NotImplementedException();
    }
}
