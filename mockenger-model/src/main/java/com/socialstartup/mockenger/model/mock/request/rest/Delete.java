package com.socialstartup.mockenger.model.mock.request.rest;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.AbstractMockRequest;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Delete extends AbstractMockRequest {
    public Delete() {
        setMethod(RequestType.DELETE);
    }
}
