package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.soap.PostService;
import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
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
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/SOAP/{groupId}"})
public class SoapController extends ParentController {

    @Autowired
    @Qualifier("soapPostService")
    private PostService postService;


    /**
     * Constructor with default content-type for responses
     */
//    public SoapController() {
//        getResponseHeaders().set("Content-Type", "application/soap+xml;charset=UTF-8");
//    }

    /**
     *
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = POST)
    public void processPosRequest() {
        throw new BadContentTypeException("Invalid header 'Content-type': application/soap+xml is only allowed in SOAP requests");
    }

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
    @RequestMapping(value = "/**", method = POST, consumes = "application/soap+xml")
    public ResponseEntity processPostRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        String soapBody = null;
        Group group = findGroupById(groupId);
        try {
            soapBody = postService.getSoapBody(requestBody, true);
        } catch (SOAPException e) {
            throw new MockObjectNotCreatedException("Cannot create SOAP message", e);
        } catch (TransformerException e) {
            throw new MockObjectNotCreatedException("An error occurred during request transformation", e);
        } catch (IOException e) {
            throw new MockObjectNotCreatedException("Cannot read xml from the provided source", e);
        }
        AbstractRequest mockRequest = postService.createMockRequest(group.getId(), soapBody, request);
        return findMockedEntities(mockRequest, group);
    }
}
