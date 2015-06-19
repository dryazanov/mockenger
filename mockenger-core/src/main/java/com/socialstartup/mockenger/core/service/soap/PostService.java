package com.socialstartup.mockenger.core.service.soap;

import com.socialstartup.mockenger.commons.utils.XmlHelper;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.PostEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.transformer.RegexpTransformer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by x079089 on 3/24/2015.
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
    public PostEntity createMockRequest(String groupId, String soapBody, HttpServletRequest request) {
        Body body = new Body(soapBody);
        return (PostEntity) fillUpEntity(new PostEntity(body), groupId, request);
    }

    /*public String getSoapBody2(String requestBody) throws SOAPException, TransformerException {
        StringReader stringReader = new StringReader(requestBody);
        Source source = new StreamSource(stringReader);
        SOAPMessage soapMessage = XmlHelper.soapToXmlConverter(source);
        return XmlHelper.xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }*/

    /**
     *
     * @param requestBody
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws IOException
     */
    public String getSoapBody(String requestBody, boolean removeWhitespaces) throws SOAPException, TransformerException, IOException {
        if (removeWhitespaces) {
            requestBody = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
        }

        SOAPMessage soapMessage = XmlHelper.stringToXmlConverter(requestBody);
        return XmlHelper.xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }
}
