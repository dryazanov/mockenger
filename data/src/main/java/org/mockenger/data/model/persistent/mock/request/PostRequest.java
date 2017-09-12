package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

import static org.mockenger.data.model.dict.RequestMethod.POST;

/**
 * @author Dmitry Ryazanov
 */
@Document
public class PostRequest extends AbstractRequest {
    public PostRequest() {
        setMethod(POST);
    }

    public PostRequest(final Body body) {
        this();
        setBody(body);
    }
}
