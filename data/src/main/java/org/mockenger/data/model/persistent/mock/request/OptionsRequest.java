package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dmitry Ryazanov
 */
@Document
public class OptionsRequest extends BodilessRequest {
    public OptionsRequest() {
        setMethod(RequestMethod.OPTIONS);
    }
}
