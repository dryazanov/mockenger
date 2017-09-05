package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
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
import java.util.SortedSet;

import static java.util.Objects.isNull;
import static org.mockenger.core.util.CommonUtils.cleanUpObject;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.util.CommonUtils.joinParams;
import static org.mockenger.core.util.CommonUtils.keysToLowercase;
import static org.mockenger.core.util.HttpUtils.getParameterSortedSet;
import static org.mockenger.core.util.MockRequestUtils.getBodyValue;
import static org.mockenger.core.util.MockRequestUtils.isURLEncodedForm;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class RequestController extends AbstractController {

    /**
     * Gets specific mock-request by ID
     *
     * @param projectCode
     * @param groupCode
     * @param requestCode
     * @return
     */
    @GetMapping(REQUEST_CODE_ENDPOINT)
    public ResponseEntity getRequest(@PathVariable final String projectCode,
                                     @PathVariable final String groupCode,
                                     @PathVariable final String requestCode) {

        findProjectByCode(projectCode);
        findGroupByCode(groupCode);

        return okResponseWithDefaultHeaders(findRequestByCode(requestCode));
    }


    /**
     * Creates mock-request
     *
     * @param projectCode
     * @param groupCode
     * @param request
     * @param result
     * @return HttpStatus.OK with created object in the response body
     */
    @PostMapping(REQUESTS_ENDPOINT)
    public ResponseEntity addRequest(@PathVariable final String projectCode,
                                     @PathVariable final String groupCode,
                                     @Valid @RequestBody final AbstractRequest request,
                                     final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Project project = findProjectByCode(projectCode);
        final Group group = findGroupByCode(groupCode);

        // Set id to null to create new mock
        request.setId(null);

        // Set creation date
        request.setCreationDate(new Date());

        // Set latency
		request.setLatency(cleanUpObject(request.getLatency()));

        // Generate new unique code
		request.setCode(getUniqueCode(project, group));

		final Headers requestHeaders = request.getHeaders();

        // Change headers to lowercase
		request.setHeaders(processHeaders(requestHeaders));

        return okResponseWithDefaultHeaders(
        		processBodyAndSave(request, isURLEncodedForm(requestHeaders)));
    }


	/**
     * Updates existing mock-request
     *
     * @param projectCode
     * @param groupCode
     * @param requestCode
     * @param request
     * @return
     */
    @PutMapping(REQUEST_CODE_ENDPOINT)
    public ResponseEntity saveRequest(@PathVariable final String projectCode,
                                      @PathVariable final String groupCode,
                                      @PathVariable final String requestCode,
                                      @Valid @RequestBody final AbstractRequest request,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        findProjectByCode(projectCode);
        findGroupByCode(groupCode);

        // If mock is found that means that id and unique code were not changed
        final AbstractRequest existingRequest = findRequestByCode(requestCode);

		if (!existingRequest.getId().equals(request.getId())) {
			throw new IllegalArgumentException("MockRequest IDs in the URL and in the payload are not equals");
		}

        // Creation date can't be changed by user
        request.setCreationDate(existingRequest.getCreationDate());
        request.setLastUpdateDate(new Date());

		// Set latency
		request.setLatency(cleanUpObject(request.getLatency()));

		final Headers requestHeaders = request.getHeaders();

		// Change headers to lowercase
		request.setHeaders(processHeaders(requestHeaders));

		return okResponseWithDefaultHeaders(
				processBodyAndSave(request, isURLEncodedForm(requestHeaders)));
    }


    /**
     * Deletes existing mock-request
     *
     * @param projectCode
     * @param groupCode
     * @param requestCode
     * @return
     */
    @DeleteMapping(REQUEST_CODE_ENDPOINT)
    public ResponseEntity deleteRequest(@PathVariable final String projectCode,
                                        @PathVariable final String groupCode,
                                        @PathVariable final String requestCode) {

        findProjectByCode(projectCode);
        findGroupByCode(groupCode);
        requestService.remove(findRequestByCode(requestCode));

        return noContentWithDefaultHeaders();
    }


    /**
     * Gets all the mock-request by provided group ID
     *
     * @param projectCode
     * @param groupCode
     * @return
     */
    @GetMapping(REQUESTS_ENDPOINT)
    public ResponseEntity getRequestList(@PathVariable final String projectCode, @PathVariable final String groupCode) {
        findProjectByCode(projectCode);

        final Group group = findGroupByCode(groupCode);
        final Iterable<AbstractRequest> requestList = requestService.findByGroupId(group.getId());

        return okResponseWithDefaultHeaders(requestList);
    }


	private Headers processHeaders(final Headers headers) {
		return new Headers(headers.getTransformers(), keysToLowercase(headers.getValues()));
	}


	private GenericRequest processBodyAndSave(final AbstractRequest request, final boolean isURLEncodedForm) {
		if (isURLEncodedForm) {
			final SortedSet sortedSet = getParameterSortedSet(getBodyValue(request));
			final String joinedParams = joinParams(sortedSet, "=", "&");

			if (isNull(request.getBody())) {
				request.setBody(new Body());
			}

			request.getBody().setValue(joinedParams);
		}

		request.setCheckSum(getCheckSum(request));

		// Save
		return requestService.save(request);
	}
}
