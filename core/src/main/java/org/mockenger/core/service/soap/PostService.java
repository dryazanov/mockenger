package org.mockenger.core.service.soap;

import org.mockenger.commons.utils.XmlHelper;
import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component(value = "soapPostService")
public class PostService extends RequestService {
    /**
     *
     * @param groupId
     * @param soapBody
     * @param request
     * @return
     */
    public PostRequest createMockRequest(final String groupId, final String soapBody, final HttpServletRequest request) {
        final Body body = new Body(soapBody);
        final PostRequest postRequest = new PostRequest(body);
        return (PostRequest) fillUpEntity(postRequest, groupId, request);
    }

    /**
     *
     * @param requestBody
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws IOException
     */
    public String getSoapBody(final String requestBody) throws SOAPException, TransformerException, IOException {
        final SOAPMessage soapMessage = XmlHelper.stringToXmlConverter(prepareRequestXmlBody(requestBody));
        return XmlHelper.xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }
}
