package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
 */
@Document
public class PatchRequest extends AbstractRequest {
    public PatchRequest() {
        setMethod(RequestMethod.PATCH);
    }

    public PatchRequest(Body body) {
        this();
        setBody(body);
    }
}
