package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@Document
public class PutRequest extends AbstractRequest {
    public PutRequest() {
        setMethod(RequestMethod.PUT);
    }

    public PutRequest(Body body) {
        this();
        setBody(body);
    }
}
