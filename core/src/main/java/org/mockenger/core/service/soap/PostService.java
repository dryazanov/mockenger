package org.mockenger.core.service.soap;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.mockenger.commons.utils.XmlHelper.stringToXmlConverter;
import static org.mockenger.commons.utils.XmlHelper.xmlToStringConverter;
import static org.mockenger.core.util.MockRequestUtils.prepareXmlBody;

/**
 * @author Dmitry Ryazanov
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

        return fillUpEntity(postRequest, groupId, request);
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
        final SOAPMessage soapMessage = stringToXmlConverter(prepareXmlBody(requestBody));

        return xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }
}
