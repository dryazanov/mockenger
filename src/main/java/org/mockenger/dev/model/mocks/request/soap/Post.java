package org.mockenger.dev.model.mocks.request.soap;

import org.mockenger.dev.model.mocks.request.AbstractMockRequest;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by x079089 on 3/12/2015.
 */
public class Post extends AbstractMockRequest {

    private String soapHeader;

    private String soapBody;


    public Post() {
        setMethod(RequestMethod.POST);
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
