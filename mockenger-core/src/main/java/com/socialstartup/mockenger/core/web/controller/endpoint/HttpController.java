package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.http.ConnectService;
import com.socialstartup.mockenger.core.service.http.HeadService;
import com.socialstartup.mockenger.core.service.http.OptionsService;
import com.socialstartup.mockenger.core.service.http.PatchService;
import com.socialstartup.mockenger.core.service.http.PostService;
import com.socialstartup.mockenger.core.service.http.PutService;
import com.socialstartup.mockenger.core.service.http.TraceService;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Profile;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.socialstartup.mockenger.data.model.dict.RequestMethod.CONNECT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/http/{groupId}"})
public class HttpController extends ParentController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(HttpController.class);

    @Autowired
    @Qualifier("httpPostService")
    private PostService postService;

    @Autowired
    @Qualifier("httpPutService")
    private PutService putService;

    @Autowired
    private PatchService patchService;

    @Autowired
    private HeadService headService;

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private TraceService traceService;

    @Autowired
    private ConnectService connectService;


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
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = HEAD)
    public ResponseEntity processHeadRequest(@PathVariable String groupId, HttpServletRequest request) {
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = headService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = OPTIONS)
    public ResponseEntity processOptionsRequest(@PathVariable String groupId, HttpServletRequest request) {
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = optionsService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**", method = TRACE)
    public ResponseEntity processTraceRequest(@PathVariable String groupId, HttpServletRequest request) {
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = traceService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/**")
    public ResponseEntity processOtherRequests(@PathVariable String groupId, HttpServletRequest request) {
        if (request.getMethod().equals(CONNECT)) {
            Profile group = findGroupById(groupId);
            AbstractRequest mockRequest = connectService.createMockRequest(group.getId(), request);
            return findMockedEntities(mockRequest, group.isRecording());
        }
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
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
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = postService.createMockRequest(group.getId(), requestBody, request);
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
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = putService.createMockRequest(group.getId(), requestBody, request);
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
    @RequestMapping(value = "/**", method = PATCH)
    public ResponseEntity processPatchRequest(@PathVariable String groupId, @RequestBody String requestBody, HttpServletRequest request) {
        Profile group = findGroupById(groupId);
        AbstractRequest mockRequest = patchService.createMockRequest(group.getId(), requestBody, request);
        return findMockedEntities(mockRequest, group.isRecording());
    }
}
