package com.socialstartup.mockenger.model.mock.request;

import com.socialstartup.mockenger.model.RequestType;

/**
 * Created by x079089 on 3/22/2015.
 */
public interface IMockRequest {

    String getGroupId();

    void setGroupId(String groupId);

    RequestType getMethod();

    void setMethod(RequestType method);
}
