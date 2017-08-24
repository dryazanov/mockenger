package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
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
