package com.socialstartup.mockenger.model.mock.request.soap;

import com.socialstartup.mockenger.model.RequestType;
import com.socialstartup.mockenger.model.mock.request.AbstractMockRequest;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Post extends AbstractMockRequest {

    private String soapHeader;

    private String soapBody;


    public Post() {
        setMethod(RequestType.POST);
    }

    public String getSoapHeader() {
        return soapHeader;
    }

    public void setSoapHeader(String soapHeader) {
        this.soapHeader = soapHeader;
    }

    public String getSoapBody() {
        return soapBody;
    }

    public void setSoapBody(String soapBody) {
        this.soapBody = soapBody;
    }
}
