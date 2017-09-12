package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.service.http.PostService;
import org.mockenger.core.web.exception.BadContentTypeException;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.MockRequestUtils.APPLICATION_XML_PATTERN;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = API_PATH + "/SOAP/{groupCode}/**")
public class SoapController extends ParentController {

	@Autowired
    @Qualifier("httpPostService")
    private PostService postService;


    /**
     *
     * @param groupCode
     * @param requestBody
     * @param request
     * @return
     */
    @PostMapping
    public Callable<ResponseEntity> processPostRequest(@PathVariable final String groupCode,
													   @RequestBody(required = false) final String requestBody,
													   final HttpServletRequest request) {

		return () -> {
			if (!isValidContentType(request)) {
				throw new BadContentTypeException("Content-type is not recognized as a valid xml type");
			}

			final Group group = findGroupByCode(groupCode);
			final GenericRequest mockRequest = postService.createGenericRequest(group.getId(), requestBody, request);

			return findMockedEntities(mockRequest, group);
		};
    }


	private boolean isValidContentType(final HttpServletRequest servletRequest) {
		return ofNullable(servletRequest.getHeader(CONTENT_TYPE))
				.map(h -> h.matches(APPLICATION_XML_PATTERN))
				.orElse(false);
	}
}
