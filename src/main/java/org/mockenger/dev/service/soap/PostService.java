package org.mockenger.dev.service.soap;

import org.mockenger.dev.common.HttpUtils;
import org.mockenger.dev.common.XmlHelper;
import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.model.mocks.request.part.Body;
import org.mockenger.dev.model.mocks.request.part.Headers;
import org.mockenger.dev.model.mocks.request.part.Parameters;
import org.mockenger.dev.model.mocks.request.part.Path;
import org.mockenger.dev.model.mocks.request.soap.PostEntity;
import org.mockenger.dev.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component(value = "soapPostService")
public class PostService<T extends IRequestEntity> extends CommonService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PostService.class);


    public T createMockRequest(String groupId, String soapBody, HttpServletRequest request) {
        String checkSum = DigestUtils.md5DigestAsHex(soapBody.getBytes());

        Body body = new Body(soapBody);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        T mockRequest = (T) new PostEntity();
        mockRequest.setGroupId(groupId);
        mockRequest.setBody(body);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(checkSum);

        return mockRequest;
    }

    public String getSoapBody(String requestBody) throws SOAPException, TransformerException {
        StringReader stringReader = new StringReader(requestBody);
        Source source = new StreamSource(stringReader);
        SOAPMessage soapMessage = XmlHelper.soapToXmlConverter(source);

        return XmlHelper.xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }
}
