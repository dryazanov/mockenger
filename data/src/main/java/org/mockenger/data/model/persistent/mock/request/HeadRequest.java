package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
 */
@Document
public class HeadRequest extends BodilessRequest {
    public HeadRequest() {
        setMethod(RequestMethod.HEAD);
    }
}
