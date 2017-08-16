package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@Document
public class PostRequest extends AbstractRequest {
    public PostRequest() {
        setMethod(RequestMethod.POST);
    }

    public PostRequest(Body body) {
        this();
        setBody(body);
    }
}
