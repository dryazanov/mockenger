package org.mockenger.dev.model.mocks.request.rest;

import org.mockenger.dev.model.mocks.request.AbstractMockRequest;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Get extends AbstractMockRequest {
    public Get() {
        setMethod(RequestMethod.GET);
    }
}
