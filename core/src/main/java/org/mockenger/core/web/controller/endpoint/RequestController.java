package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.util.CommonUtils.keysToLowercase;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class RequestController extends AbstractController {

    /**
     * Gets specific mock-request by ID
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @return
     */
    @GetMapping(REQUEST_ID_ENDPOINT)
    public ResponseEntity getRequest(@PathVariable final String projectId,
                                     @PathVariable final String groupId,
                                     @PathVariable final String requestId) {

        findProjectById(projectId);
        findGroupById(groupId);

        return okResponseWithDefaultHeaders(findRequestById(requestId));
    }


    /**
     * Creates mock-request
     *
     * @param projectId
     * @param groupId
     * @param request
     * @param result
     * @return HttpStatus.OK with created object in the response body
     */
    @PostMapping(REQUESTS_ENDPOINT)
    public ResponseEntity addRequest(@PathVariable final String projectId,
                                     @PathVariable final String groupId,
                                     @Valid @RequestBody final AbstractRequest request,
                                     final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // TODO: Maybe we can create validation with the chain: check request -> check group -> check project
        final Project project = findProjectById(projectId);
        findGroupById(groupId);

        // Set id to null to create new mock
        request.setId(null);

        // Set creation date
        request.setCreationDate(new Date());

        // Generate new unique code
        request.setUniqueCode(String.format("%s-%d", project.getCode(), getProjectService().getNextSequenceValue(projectId)));

        // Change headers to lowercase
		request.setHeaders(processHeaders(request.getHeaders()));

        // Remove whitespaces
        cleanUpRequestBody(request);

        // Generate checksum
        request.setCheckSum(getCheckSum(request));

        // Save
        getRequestService().save(request);

        return okResponseWithDefaultHeaders(request);
    }


	/**
     * Updates existing mock-request
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @param request
     * @return
     */
    @PutMapping(REQUEST_ID_ENDPOINT)
    public ResponseEntity saveRequest(@PathVariable final String projectId,
                                      @PathVariable final String groupId,
                                      @PathVariable final String requestId,
                                      @Valid @RequestBody final AbstractRequest request,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        findProjectById(projectId);
        findGroupById(groupId);

        // If mock found that means that id and unique code were not changed
        final AbstractRequest fountRequest = findRequestByIdAndUniqueCode(requestId, request.getUniqueCode());

        // Creation date can't be changed by user
        request.setCreationDate(fountRequest.getCreationDate());
        request.setLastUpdateDate(new Date());

		// Change headers to lowercase
		request.setHeaders(processHeaders(request.getHeaders()));

        // Remove whitespaces
        cleanUpRequestBody(request);

        // Re-generate checksum because values could be updated
        request.setCheckSum(getCheckSum(request));

        // Save
        getRequestService().save(request);

        return okResponseWithDefaultHeaders(request);
    }


    /**
     * Deletes existing mock-request
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @return
     */
    @DeleteMapping(REQUEST_ID_ENDPOINT)
    public ResponseEntity deleteRequest(@PathVariable final String projectId,
                                        @PathVariable final String groupId,
                                        @PathVariable final String requestId) {

        findProjectById(projectId);
        findGroupById(groupId);
        getRequestService().remove(findRequestById(requestId));

        return noContentWithDefaultHeaders();
    }


    /**
     * Gets all the mock-request by provided group ID
     *
     * @param projectId
     * @param groupId
     * @return
     */
    @GetMapping(REQUESTS_ENDPOINT)
    public ResponseEntity getRequestList(@PathVariable final String projectId, @PathVariable final String groupId) {
        findProjectById(projectId);

        final Group group = findGroupById(groupId);
        final Iterable<AbstractRequest> requestList = getRequestService().findByGroupId(group.getId());

        return okResponseWithDefaultHeaders(requestList);
    }


	private Headers processHeaders(final Headers headers) {
		return new Headers(headers.getTransformers(), keysToLowercase(headers.getValues()));
	}
}
