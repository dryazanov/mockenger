package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.http.ConnectService;
import com.socialstartup.mockenger.core.service.http.HeadService;
import com.socialstartup.mockenger.core.service.http.OptionsService;
import com.socialstartup.mockenger.core.service.http.PatchService;
import com.socialstartup.mockenger.core.service.http.PostService;
import com.socialstartup.mockenger.core.service.http.PutService;
import com.socialstartup.mockenger.core.service.http.TraceService;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static com.socialstartup.mockenger.data.model.dict.RequestMethod.CONNECT;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = API_PATH + "/HTTP/{groupId}/**")
public class HttpController extends ParentController {

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
    @GetMapping
    public ResponseEntity processGetRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        return doGetRequest(groupId, request);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @DeleteMapping
    public ResponseEntity processDeleteRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        return doDeleteRequest(groupId, request);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @RequestMapping(method = HEAD)
    public ResponseEntity processHeadRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = headService.createMockRequest(group.getId(), request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @RequestMapping(method = OPTIONS)
    public ResponseEntity processOptionsRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = optionsService.createMockRequest(group.getId(), request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @RequestMapping(method = TRACE)
    public ResponseEntity processTraceRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = traceService.createMockRequest(group.getId(), request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @RequestMapping
    public ResponseEntity processOtherRequests(@PathVariable final String groupId, final HttpServletRequest request) {
        if (CONNECT.equals(request.getMethod())) {
            final Group group = findGroupById(groupId);
            final GenericRequest mockRequest = connectService.createMockRequest(group.getId(), request);

            return findMockedEntities(mockRequest, group);
        }

        return notFoundWithDefaultHeaders();
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity processPostRequest(@PathVariable final String groupId,
											 @RequestBody final String requestBody,
											 final HttpServletRequest request) {

        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = postService.createGenericRequest(group.getId(), requestBody, request);
		cleanUpRequestBody(mockRequest);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity processPutRequest(@PathVariable final String groupId,
											@RequestBody final String requestBody,
											final HttpServletRequest request) {

        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = putService.createMockRequest(group.getId(), requestBody, request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @RequestMapping(method = PATCH)
    public ResponseEntity processPatchRequest(@PathVariable final String groupId,
											  @RequestBody final String requestBody,
											  final HttpServletRequest request) {

        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = patchService.createMockRequest(group.getId(), requestBody, request);

        return findMockedEntities(mockRequest, group);
    }
}
