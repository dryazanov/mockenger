package org.mockenger.dev.model.mock.request.rest;

import org.mockenger.dev.model.mock.request.AbstractMockRequest;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Post extends AbstractMockRequest {
    public Post() {
        setMethod(RequestMethod.POST);
    }
}
