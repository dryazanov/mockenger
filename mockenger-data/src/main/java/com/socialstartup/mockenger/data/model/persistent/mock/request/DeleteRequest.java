package com.socialstartup.mockenger.data.model.persistent.mock.request;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dmitry Ryazanov on 3/12/2015.
 */
@Document
public class DeleteRequest extends BodilessRequest {
    public DeleteRequest() {
        setMethod(RequestMethod.DELETE);
    }
}
