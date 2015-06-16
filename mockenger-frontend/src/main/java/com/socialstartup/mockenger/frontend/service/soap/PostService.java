package com.socialstartup.mockenger.frontend.service.soap;

import com.socialstartup.mockenger.commons.utils.XmlHelper;
import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.frontend.service.CommonService;
import com.socialstartup.mockenger.model.mock.request.entity.PostEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import org.springframework.stereotype.Component;

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
public class PostService extends CommonService {

    public PostEntity createMockRequest(String groupId, String soapBody, HttpServletRequest request) {
        Body body = new Body(soapBody);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        PostEntity mockRequest = new PostEntity();
        mockRequest.setGroupId(groupId);
        mockRequest.setBody(body);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }

    public String getSoapBody(String requestBody) throws SOAPException, TransformerException {
        StringReader stringReader = new StringReader(requestBody);
        Source source = new StreamSource(stringReader);
        SOAPMessage soapMessage = XmlHelper.soapToXmlConverter(source);

        return XmlHelper.xmlToStringConverter(soapMessage.getSOAPBody(), true);
    }
}
