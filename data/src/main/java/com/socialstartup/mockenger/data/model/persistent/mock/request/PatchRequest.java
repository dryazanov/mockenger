package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
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
