package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by x079089 on 6/15/2015.
 */
public abstract class BodilessRequest extends AbstractRequest {
    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public void setBody(Body body) {
        throw new NotImplementedException();
    }
}
