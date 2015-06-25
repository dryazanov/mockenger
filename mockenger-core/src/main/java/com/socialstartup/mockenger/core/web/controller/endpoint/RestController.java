package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.rest.PostService;
import com.socialstartup.mockenger.core.service.rest.PutService;
import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
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
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/rest/{groupId}"})
public class RestController extends ParentController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    @Qualifier("restPostService")
    private PostService postService;

    @Autowired
    @Qualifier("restPutService")
    private PutService putService;


    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = GET)
    public ResponseEntity processGetRequest(@PathVariable String groupId, HttpServletRequest request) {
        return doGetRequest(groupId, request);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = DELETE)
    public ResponseEntity processDeleteRequest(@PathVariable String groupId, HttpServletRequest request) {
        return doDeleteRequest(groupId, request);
    }

    /**
     *
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = {POST, PUT})
    public void processPosRequest() {
        throw new BadContentTypeException("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests.");
    }

    /**
     *
     * @param groupId
     * @param jsonBody
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = POST, consumes = "application/json")
    public ResponseEntity processPostJsonRequest(@PathVariable String groupId, @RequestBody String jsonBody, HttpServletRequest request) {
        AbstractRequest mockRequest = null;
        Group group = findGroupById(groupId);
        try {
            mockRequest = postService.createMockRequestFromJson(group.getId(), jsonBody, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = POST, consumes = "application/xml")
    public ResponseEntity processPostXmlRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        AbstractRequest mockRequest = null;
        Group group = findGroupById(groupId);

        try {
            mockRequest = postService.createMockRequestFromXml(group.getId(), requestBody, request, true);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param jsonBody
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = PUT, consumes = "application/json")
    public ResponseEntity processPutJsonRequest(@PathVariable String groupId, @RequestBody String jsonBody, HttpServletRequest request) {
        AbstractRequest mockRequest = null;
        Group group = findGroupById(groupId);
        try {
            mockRequest = putService.createMockRequestFromJson(group.getId(), jsonBody, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = PUT, consumes = "application/xml")
    public ResponseEntity processPutXmlRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        AbstractRequest mockRequest = null;
        Group group = findGroupById(groupId);

        try {
            mockRequest = putService.createMockRequestFromXml(group.getId(), requestBody, request, true);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return findMockedEntities(mockRequest, group.isRecording());
    }
}
