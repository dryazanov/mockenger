package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.mapper.request.GridRowMapper;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dto.Grid;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
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
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by x079089 on 7/3/2015.
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
     * @return
     */
    @ResponseBody
    @RequestMapping(value = REQUESTS, method = POST)
    public ResponseEntity addRequest(@PathVariable String projectId, @PathVariable String groupId, @Valid @RequestBody AbstractRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        findProjectById(projectId);
        findGroupById(groupId);
        request.setId(null);
        getRequestService().save(request);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
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
        findRequestById(requestId);
        getRequestService().save(request);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
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
        AbstractRequest request = findRequestById(requestId);
        getRequestService().remove(request);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
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

        GridRowMapper mapper = new GridRowMapper();
        Grid bootGrid = new Grid();
        bootGrid.setCurrent(1);
        bootGrid.setTotal(requestList.size());
        bootGrid.setRowCount(requestList.size());
        bootGrid.setRows(mapper.map(requestList));
        return new ResponseEntity(requestList, getResponseHeaders(), HttpStatus.OK);
    }
}
