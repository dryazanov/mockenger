package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.BodilessRequest;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by x079089 on 3/12/2015.
 */
@Document
public class OptionsRequest extends BodilessRequest {
    public OptionsRequest() {
        setMethod(RequestMethod.OPTIONS);
    }
}
