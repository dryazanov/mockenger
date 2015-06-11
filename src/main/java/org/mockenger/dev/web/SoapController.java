package org.mockenger.dev.web;

import org.mockenger.dev.common.CommonUtils;
import org.mockenger.dev.model.mocks.group.GroupEntity;
import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.model.transformer.ITransformer;
import org.mockenger.dev.model.transformer.RegexpTransformer;
import org.mockenger.dev.service.soap.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/soap/{groupId}"})
public class SoapController extends MainController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(SoapController.class);


    @Autowired
    @Qualifier("soapPostService")
    private PostService postService;


    /**
     *
     * @param requestBody
     * @param groupId
     * @param request
     * @return
     * @throws SOAPException
     * @throws TransformerException
     * @throws FileNotFoundException
     */
    @ResponseBody
    @RequestMapping(value = {"/**"}, method = POST)
    public ResponseEntity processPostRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {

        GroupEntity group = findGroupById(groupId);

        ITransformer transformer = new RegexpTransformer(">\\s+<", "><");
        requestBody = transformer.transform(requestBody.trim());

        String soapBody = null;
        try {
            soapBody = postService.getSoapBody(requestBody);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        IRequestEntity mockRequest = postService.createMockRequest(group.getId(), soapBody, request);
        IRequestEntity mockResult = getRequestService().findMockedEntities(mockRequest);

        if (mockResult != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_XML_VALUE);
            // TODO: Check mockResult.getResponse().getResponseBody() for null values
            return new ResponseEntity(mockResult.getResponse().getResponseBody(), getResponseHeaders(), mockResult.getResponse().getHttpStatus());
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (group.isRecordingStarted()) {
                // TODO: Decide which unique id generator is better
                mockRequest.setId(CommonUtils.generateUniqueId());
                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            }
            return new ResponseEntity(getResponseHeaders(), status);
        }
    }
}
