package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.service.soap.PostService;
import org.mockenger.core.web.exception.MockObjectNotCreatedException;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.core.web.controller.base.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = AbstractController.API_PATH + "/SOAP/{groupId}/**")
public class SoapController extends ParentController {

	@Autowired
    @Qualifier("soapPostService")
    private PostService postService;


    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    @PostMapping
    public Callable<ResponseEntity> processPostRequest(@PathVariable final String groupId,
													   @RequestBody final String requestBody,
													   final HttpServletRequest request) {
		return () -> {
			final Group group = findGroupById(groupId);

			try {
				final String soapBody = postService.getSoapBody(requestBody);
				final GenericRequest mockRequest = postService.createMockRequest(group.getId(), soapBody, request);

				return findMockedEntities(mockRequest, group);
			} catch (SOAPException e) {
				throw new MockObjectNotCreatedException("Cannot create SOAP message", e);
			} catch (TransformerException e) {
				throw new MockObjectNotCreatedException("An error occurred during request transformation", e);
			} catch (IOException e) {
				throw new MockObjectNotCreatedException("Cannot read xml from the provided source", e);
			}
		};
    }
}
