package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
 */
@Document
public class GetRequest extends BodilessRequest {
    public GetRequest() {
        setMethod(RequestMethod.GET);
    }
}
