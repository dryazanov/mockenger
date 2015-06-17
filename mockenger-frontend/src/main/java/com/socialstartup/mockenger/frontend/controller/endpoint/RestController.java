package com.socialstartup.mockenger.frontend.controller.endpoint;

import com.socialstartup.mockenger.frontend.service.rest.PostService;
import com.socialstartup.mockenger.frontend.service.rest.PutService;
import com.socialstartup.mockenger.data.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
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
}
