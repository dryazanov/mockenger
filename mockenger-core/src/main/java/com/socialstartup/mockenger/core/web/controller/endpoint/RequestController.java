package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Dmitry Ryazanov on 7/3/2015.
 */
@Controller
public class RequestController extends AbstractController {

    private static final String REQUESTS = PROJECT_ID_ENDPOINT + GROUP_ID_ENDPOINT + REQUESTS_ENDPOINT;
    private static final String REQUESTID = PROJECT_ID_ENDPOINT + GROUP_ID_ENDPOINT + REQUEST_ID_ENDPOINT;


    /**
     * Gets one mock-request by provided ID
     *
     * @param projectId
     * @param groupId
     * @param requestId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = REQUESTID, method = GET)
    public ResponseEntity getRequest(@PathVariable String projectId, @PathVariable String groupId, @PathVariable String requestId) {
        findProjectById(projectId);
        findGroupById(groupId);
        return new ResponseEntity(findRequestById(requestId), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Adds mock-request
     *
     * @param projectId
     * @param groupId
     * @param request
     * @param result
     * @return HttpStatus.OK with created object in the response body
     */
    @ResponseBody
    @RequestMapping(value = REQUESTS, method = POST)
    public ResponseEntity addRequest(@PathVariable String projectId, @PathVariable String groupId,
                                     @Valid @RequestBody AbstractRequest request, BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // TODO: Maybe we can create validation with the chain: check request -> check group -> check project
        Project project = findProjectById(projectId);
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
    @ResponseBody
    @RequestMapping(value = REQUESTID, method = PUT)
    public ResponseEntity saveRequest(@PathVariable String projectId, @PathVariable String groupId, @PathVariable String requestId,
                                      @Valid @RequestBody AbstractRequest request, BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        findProjectById(projectId);
        findGroupById(groupId);

        // If mock found that means that id and unique code were not changed
        AbstractRequest fountRequest = findRequestByIdAndUniqueCode(requestId, request.getUniqueCode());
        // Creation date can't be changed by user
        request.setCreationDate(fountRequest.getCreationDate());
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
    @ResponseBody
    @RequestMapping(value = REQUESTID, method = DELETE)
    public ResponseEntity deleteRequest(@PathVariable String projectId, @PathVariable String groupId, @PathVariable String requestId) {
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
    @ResponseBody
    @RequestMapping(value = REQUESTS, method = GET)
    public ResponseEntity getRequestList(@PathVariable String projectId, @PathVariable String groupId) {
        findProjectById(projectId);
        Group group = findGroupById(groupId);

        List<AbstractRequest> requestList = getRequestService().findByGroupId(group.getId());

        if (requestList == null) {
            requestList = new ArrayList<>();
        }

        return new ResponseEntity(requestList, getResponseHeaders(), HttpStatus.OK);
    }
}
