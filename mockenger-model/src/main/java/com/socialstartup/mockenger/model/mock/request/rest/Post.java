package com.socialstartup.mockenger.model.mock.request.rest;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.AbstractMockRequest;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Post extends AbstractMockRequest {
    public Post() {
        setMethod(RequestType.POST);
    }
}
