package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.service.rest.PostService;
import org.mockenger.core.service.rest.PutService;
import org.mockenger.core.web.exception.BadContentTypeException;
import org.mockenger.core.web.exception.MockObjectNotCreatedException;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.core.web.controller.base.AbstractController;
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
import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = AbstractController.API_PATH + "/REST/{groupId}/**")
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
    @GetMapping
    public Callable<ResponseEntity> processGetRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        return () -> doGetRequest(groupId, request);
    }


    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    @DeleteMapping
    public Callable<ResponseEntity> processDeleteRequest(@PathVariable final String groupId, final HttpServletRequest request) {
        return () -> doDeleteRequest(groupId, request);
    }


    /**
     *
     */
    @RequestMapping(method = {POST, PUT})
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
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity> processPostJsonRequest(@PathVariable final String groupId,
														   @RequestBody final String jsonBody,
														   final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupById(groupId);

			try {
				final GenericRequest mockRequest = postService.createMockRequestFromJson(group.getId(), jsonBody, request);
				return findMockedEntities(mockRequest, group);
			} catch (IOException e) {
				throw new MockObjectNotCreatedException("Cannot read json from the provided source", e);
			}
		};
    }


    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @PostMapping(consumes = APPLICATION_XML_VALUE)
    public Callable<ResponseEntity> processPostXmlRequest(@PathVariable final String groupId,
														  @RequestBody final String requestBody,
														  final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupById(groupId);
			final GenericRequest mockRequest = postService.createMockRequestFromXml(group.getId(), requestBody, request);

			return findMockedEntities(mockRequest, group);
		};
    }


    /**
     *
     * @param groupId
     * @param jsonBody
     * @param request
     * @return
     */
    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity> processPutJsonRequest(@PathVariable final String groupId,
														  @RequestBody final String jsonBody,
														  final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupById(groupId);

			try {
				final GenericRequest mockRequest = putService.createMockRequestFromJson(group.getId(), jsonBody, request);
				return findMockedEntities(mockRequest, group);
			} catch (IOException e) {
				throw new MockObjectNotCreatedException("Cannot read json from the provided source", e);
			}
		};
    }


    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @PutMapping(consumes = APPLICATION_XML_VALUE)
    public Callable<ResponseEntity> processPutXmlRequest(@PathVariable final String groupId,
														 @RequestBody final String requestBody,
														 final HttpServletRequest request) {
		return () -> {
			final Group group = findGroupById(groupId);
			final GenericRequest mockRequest = putService.createMockRequestFromXml(group.getId(), requestBody, request);

			return findMockedEntities(mockRequest, group);
		};
    }
}
