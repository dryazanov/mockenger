package org.mockenger.dev.controller.endpoint;

import org.mockenger.dev.common.CommonUtils;
import org.mockenger.dev.controller.web.MainController;
import org.mockenger.dev.model.mock.group.GroupEntity;
import org.mockenger.dev.model.mock.request.IRequestEntity;
import org.mockenger.dev.service.rest.DeleteService;
import org.mockenger.dev.service.rest.GetService;
import org.mockenger.dev.service.rest.PostService;
import org.mockenger.dev.service.rest.PutService;
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
        IRequestEntity mockRequest = getService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecordingStarted());
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
        IRequestEntity mockRequest = null;
        GroupEntity group = findGroupById(groupId);

        try {
            mockRequest = postService.createMockRequest(group.getId(), requestBody, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return findMockedEntities(mockRequest, group.isRecordingStarted());
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
        IRequestEntity mockRequest = null;
        GroupEntity group = findGroupById(groupId);

        try {
            mockRequest = putService.createMockRequest(group.getId(), requestBody, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return findMockedEntities(mockRequest, group.isRecordingStarted());
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
        IRequestEntity mockRequest = deleteService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecordingStarted());
    }


    /**
     *
     * @param mockRequest
     * @param recordRequests
     * @return
     */
    private ResponseEntity findMockedEntities(IRequestEntity mockRequest, boolean recordRequests) {
        if (mockRequest == null) {
            // TODO: Create and throw MockObjectNotCreatedException
            throw new RuntimeException("Can't create mock object");
        }

        IRequestEntity mockResult = getRequestService().findMockedEntities(mockRequest);
        return generateResponse(mockRequest, mockResult, recordRequests);
    }

    /**
     *
     * @param mockRequest
     * @param mockResult
     * @param recordRequests
     * @return
     */
    private ResponseEntity generateResponse(IRequestEntity mockRequest, IRequestEntity mockResult, boolean recordRequests) {
        if (mockResult != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            // TODO: Check mockResult.getResponse().getResponseBody() for null values
            return new ResponseEntity(mockResult.getResponse().getResponseBody(), getResponseHeaders(), mockResult.getResponse().getHttpStatus());
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
