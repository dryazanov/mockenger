package com.socialstartup.mockenger.frontend.controller.endpoint;

import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.controller.web.MainController;
import com.socialstartup.mockenger.frontend.service.rest.DeleteService;
import com.socialstartup.mockenger.frontend.service.rest.GetService;
import com.socialstartup.mockenger.frontend.service.rest.PostService;
import com.socialstartup.mockenger.frontend.service.rest.PutService;
import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/rest/{groupId}"})
public class RestController extends MainController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

    @Autowired
    private PutService putService;

    @Autowired
    private DeleteService deleteService;


    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = GET)
    public ResponseEntity processGetRequest(@PathVariable String groupId, HttpServletRequest request) {
        GroupEntity group = findGroupById(groupId);
        RequestEntity mockRequest = getService.createMockRequest(group.getId(), request);
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
    @RequestMapping(value = "/**", method = POST)
    public ResponseEntity processPostRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        RequestEntity mockRequest = null;
        GroupEntity group = findGroupById(groupId);

        try {
            mockRequest = postService.createMockRequest(group.getId(), requestBody, request);
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
    @RequestMapping(value = "/**", method = PUT)
    public ResponseEntity processPutRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        RequestEntity mockRequest = null;
        GroupEntity group = findGroupById(groupId);

        try {
            mockRequest = putService.createMockRequest(group.getId(), requestBody, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return findMockedEntities(mockRequest, group.isRecording());
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
        GroupEntity group = findGroupById(groupId);
        RequestEntity mockRequest = deleteService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }


    /**
     *
     * @param mockRequest
     * @param recordRequests
     * @return
     */
    private ResponseEntity findMockedEntities(RequestEntity mockRequest, boolean recordRequests) {
        if (mockRequest == null) {
            // TODO: Create and throw MockObjectNotCreatedException
            throw new RuntimeException("Can't create mock object");
        }

        RequestEntity mockResult = getRequestService().findMockedEntities(mockRequest);
        return generateResponse(mockRequest, mockResult, recordRequests);
    }

    /**
     *
     * @param mockRequest
     * @param mockResult
     * @param recordRequests
     * @return
     */
    private ResponseEntity generateResponse(RequestEntity mockRequest, RequestEntity mockResult, boolean recordRequests) {
        if (mockResult != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            // TODO: Check mockResult.getResponse().getResponseBody() for null values
            int httpStatusCode = mockResult.getResponse().getHttpStatus();
            return new ResponseEntity(mockResult.getResponse().getResponseBody(), getResponseHeaders(), HttpStatus.valueOf(httpStatusCode));
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (recordRequests) {
                // TODO: Decide which unique id generator is better
                mockRequest.setId(CommonUtils.generateUniqueId());
                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            }
            return new ResponseEntity(getResponseHeaders(), status);
        }
    }
}
