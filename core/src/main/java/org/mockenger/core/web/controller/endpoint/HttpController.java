package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.service.http.HeadService;
import org.mockenger.core.service.http.OptionsService;
import org.mockenger.core.service.http.PatchService;
import org.mockenger.core.service.http.PostService;
import org.mockenger.core.service.http.PutService;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

import static org.mockenger.core.web.controller.base.AbstractController.MOCK_HTTP_TYPE_PATH;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;


/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = MOCK_HTTP_TYPE_PATH + "/{groupCode}/**")
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


    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    @GetMapping
    public Callable<ResponseEntity> processGetRequest(@PathVariable final String groupCode, final HttpServletRequest request) {
        return () -> doGetRequest(groupCode, request);
    }


	/**
	 *
	 * @param groupCode
	 * @param requestBody
	 * @param request
	 * @return
	 */
	@PostMapping
	public Callable<ResponseEntity> processPostRequest(@PathVariable final String groupCode,
													   @RequestBody final String requestBody,
													   final HttpServletRequest request) {
		return () -> {
			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = postService.createGenericRequest(group.getId(), requestBody, request);
			final String cleanRequestBody = getRequestService().cleanUpRequestBody(mockRequest);

			mockRequest.getBody().setValue(cleanRequestBody);

			return findMockedEntities(mockRequest, group);
		};
	}


	/**
	 *
	 * @param groupCode
	 * @param requestBody
	 * @param request
	 * @return
	 */
	@PutMapping
	public Callable<ResponseEntity> processPutRequest(@PathVariable final String groupCode,
													  @RequestBody final String requestBody,
													  final HttpServletRequest request) {
		return () -> {
			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = putService.createMockRequest(group.getId(), requestBody, request);
			final String cleanRequestBody = getRequestService().cleanUpRequestBody(mockRequest);

			mockRequest.getBody().setValue(cleanRequestBody);

			return findMockedEntities(mockRequest, group);
		};
	}


    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    @DeleteMapping
    public Callable<ResponseEntity> processDeleteRequest(@PathVariable final String groupCode, final HttpServletRequest request) {
        return () -> doDeleteRequest(groupCode, request);
    }


    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    @RequestMapping(method = HEAD)
    public Callable<ResponseEntity> processHeadRequest(@PathVariable final String groupCode, final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = headService.createMockRequest(group.getId(), request);

			return findMockedEntities(mockRequest, group);
		};
    }


    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    @RequestMapping(method = OPTIONS)
    public Callable<ResponseEntity> processOptionsRequest(@PathVariable final String groupCode, final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = optionsService.createMockRequest(group.getId(), request);

			return findMockedEntities(mockRequest, group);
		};
    }


    /**
     *
     * @param groupCode
     * @param requestBody
     * @param request
     * @return
     */
    @PatchMapping
    public Callable<ResponseEntity> processPatchRequest(@PathVariable final String groupCode,
														@RequestBody final String requestBody,
														final HttpServletRequest request) {
    	return () -> {
			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = patchService.createMockRequest(group.getId(), requestBody, request);
			final String cleanRequestBody = getRequestService().cleanUpRequestBody(mockRequest);

			mockRequest.getBody().setValue(cleanRequestBody);

			return findMockedEntities(mockRequest, group);
		};
    }
}
