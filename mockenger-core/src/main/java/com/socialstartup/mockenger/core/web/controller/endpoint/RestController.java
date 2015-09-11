package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.rest.PostService;
import com.socialstartup.mockenger.core.service.rest.PutService;
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
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/REST/{groupId}"})
public class RestController extends ParentController {

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
        throw new BadContentTypeException("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests");
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
        Group group = findGroupById(groupId);
        try {
            AbstractRequest mockRequest = postService.createMockRequestFromJson(group.getId(), jsonBody, request);
            return findMockedEntities(mockRequest, group);
        } catch (IOException e) {
            throw new MockObjectNotCreatedException("Cannot read json from the provided source", e);
        }
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
        Group group = findGroupById(groupId);
        try {
            AbstractRequest mockRequest = postService.createMockRequestFromXml(group.getId(), requestBody, request, true);
            return findMockedEntities(mockRequest, group);
        } catch (TransformerException e) {
            throw new MockObjectNotCreatedException("Cannot transform provided xml", e);
        }
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
        Group group = findGroupById(groupId);
        try {
            AbstractRequest mockRequest = putService.createMockRequestFromJson(group.getId(), jsonBody, request);
            return findMockedEntities(mockRequest, group);
        } catch (IOException e) {
            throw new MockObjectNotCreatedException("Cannot read json from the provided source", e);
        }
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
        Group group = findGroupById(groupId);
        try {
            AbstractRequest mockRequest = putService.createMockRequestFromXml(group.getId(), requestBody, request, true);
            return findMockedEntities(mockRequest, group);
        } catch (TransformerException e) {
            throw new MockObjectNotCreatedException("Cannot transform provided xml", e);
        }
    }
}
