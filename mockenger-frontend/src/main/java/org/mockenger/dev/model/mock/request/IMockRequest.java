package org.mockenger.dev.model.mock.request;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by x079089 on 3/22/2015.
 */
public interface IMockRequest {

    String getGroupId();

    void setGroupId(String groupId);

    RequestMethod getMethod();

    void setMethod(RequestMethod method);
}
