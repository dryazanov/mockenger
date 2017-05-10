package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class RequestController extends AbstractController {

    private static final String REQUESTS = PROJECT_ID_ENDPOINT + GROUP_ID_ENDPOINT + REQUESTS_ENDPOINT;
    private static final String REQUESTID = PROJECT_ID_ENDPOINT + GROUP_ID_ENDPOINT + REQUEST_ID_ENDPOINT;


    /**
     * Gets specific mock-request by ID
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @return
     */
    @RequestMapping(value = REQUESTID, method = GET)
    public ResponseEntity getRequest(@PathVariable final String projectId,
                                     @PathVariable final String groupId,
                                     @PathVariable final String requestId) {

        findProjectById(projectId);
        findGroupById(groupId);

        return new ResponseEntity(findRequestById(requestId), getResponseHeaders(), HttpStatus.OK);
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
    @RequestMapping(value = REQUESTS, method = POST)
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
        // Remove whitespaces
        cleanUpRequestBody(request);
        // Generate checksum
        request.setCheckSum(CommonUtils.getCheckSum(request));
        // Save
        getRequestService().save(request);

        return new ResponseEntity(request, getResponseHeaders(), HttpStatus.OK);
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
    @RequestMapping(value = REQUESTID, method = PUT)
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
        AbstractRequest fountRequest = findRequestByIdAndUniqueCode(requestId, request.getUniqueCode());
        // Creation date can't be changed by user
        request.setCreationDate(fountRequest.getCreationDate());
        request.setLastUpdateDate(new Date());
        // Remove whitespaces
        cleanUpRequestBody(request);
        // Re-generate checksum because values could be updated
        request.setCheckSum(CommonUtils.getCheckSum(request));
        // Save
        getRequestService().save(request);

        return new ResponseEntity(request, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Deletes existing mock-request
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @return
     */
    @RequestMapping(value = REQUESTID, method = DELETE)
    public ResponseEntity deleteRequest(@PathVariable final String projectId,
                                        @PathVariable final String groupId,
                                        @PathVariable final String requestId) {

        findProjectById(projectId);
        findGroupById(groupId);
        getRequestService().remove(findRequestById(requestId));

        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     * Gets all the mock-request by provided group ID
     *
     * @param projectId
     * @param groupId
     * @return
     */
    @RequestMapping(value = REQUESTS, method = GET)
    public ResponseEntity getRequestList(@PathVariable final String projectId, @PathVariable final String groupId) {
        findProjectById(projectId);

        final Group group = findGroupById(groupId);
        final Iterable<AbstractRequest> requestList = getRequestService().findByGroupId(group.getId());

        return new ResponseEntity(requestList, getResponseHeaders(), HttpStatus.OK);
    }
}
