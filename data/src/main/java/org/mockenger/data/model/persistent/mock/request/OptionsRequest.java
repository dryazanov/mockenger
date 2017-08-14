package org.mockenger.data.model.persistent.mock.request;

import org.mockenger.data.model.dict.RequestMethod;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@Document
public class OptionsRequest extends BodilessRequest {
    public OptionsRequest() {
        setMethod(RequestMethod.OPTIONS);
    }
}
