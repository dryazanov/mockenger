package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.soap.PostService;
import com.socialstartup.mockenger.data.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.transformer.ITransformer;
import com.socialstartup.mockenger.data.model.transformer.RegexpTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class SoapController extends ParentController {

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
        RequestEntity mockRequest = postService.createMockRequest(group.getId(), soapBody, request);
        return findMockedEntities(mockRequest, group.isRecording());

//        RequestEntity mockResult = getRequestService().findMockedEntities(mockRequest);

        /*if (mockResult != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_XML_VALUE);
            // TODO: Check mockResult.getMockResponse().getBody() for null values
            int httpStatusCode = mockResult.getMockResponse().getHttpStatus();
            return new ResponseEntity(mockResult.getMockResponse().getBody(), getResponseHeaders(), HttpStatus.valueOf(httpStatusCode));
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (group.isRecording()) {
                // TODO: Decide which unique id generator is better
                mockRequest.setId(CommonUtils.generateUniqueId());
                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            }
            return new ResponseEntity(getResponseHeaders(), status);
        }*/
    }
}
