package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.persistent.mock.request.part.Body;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Dmitry Ryazanov on 6/15/2015.
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
