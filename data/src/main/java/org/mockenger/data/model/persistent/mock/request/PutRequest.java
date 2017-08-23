package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
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
