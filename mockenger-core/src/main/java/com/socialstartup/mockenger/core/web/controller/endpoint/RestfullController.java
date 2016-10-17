package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.rest.PostService;
import com.socialstartup.mockenger.core.service.rest.PutService;
import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(value = API_PATH + "/REST/{groupId}")
public class RestfullController extends ParentController {

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
    @RequestMapping(value = "/**", method = DELETE)
    public ResponseEntity processDeleteRequest(@PathVariable String groupId, HttpServletRequest request) {
        return doDeleteRequest(groupId, request);
    }

    /**
     *
     */
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
    @RequestMapping(value = "/**", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processPostJsonRequest(@PathVariable final String groupId,
												 @RequestBody final String jsonBody,
												 final HttpServletRequest request) {

        final Group group = findGroupById(groupId);

        try {
            final GenericRequest mockRequest = postService.createMockRequestFromJson(group.getId(), jsonBody, request);
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
    @RequestMapping(value = "/**", method = POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity processPostXmlRequest(@PathVariable final String groupId,
												@RequestBody final String requestBody,
												final HttpServletRequest request) {

        final Group group = findGroupById(groupId);
        final GenericRequest mockRequest = postService.createMockRequestFromXml(group.getId(), requestBody, request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param jsonBody
     * @param request
     * @return
     */
    @RequestMapping(value = "/**", method = PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processPutJsonRequest(@PathVariable final String groupId,
												@RequestBody final String jsonBody,
												final HttpServletRequest request) {

        final Group group = findGroupById(groupId);

        try {
            final GenericRequest mockRequest = putService.createMockRequestFromJson(group.getId(), jsonBody, request);
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
    @RequestMapping(value = "/**", method = PUT, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity processPutXmlRequest(@PathVariable final String groupId,
											   @RequestBody final String requestBody,
											   final HttpServletRequest request) {

        final Group group = findGroupById(groupId);
		final GenericRequest mockRequest = putService.createMockRequestFromXml(group.getId(), requestBody, request);

        return findMockedEntities(mockRequest, group);
    }
}
