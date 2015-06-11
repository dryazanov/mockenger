package org.mockenger.dev.web;

import org.mockenger.dev.model.dto.BootGridDTO;
import org.mockenger.dev.model.mocks.group.GroupEntity;
import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.service.RequestService;
import org.mockenger.dev.service.mapper.BootGridRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = "/group")
public class GroupController extends MainController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private RequestService requestService;


    /**
     *
     * @param groupEntity
     * @return
     */
    @ResponseBody
    @RequestMapping(method = POST)
    public ResponseEntity addGroup(@RequestBody GroupEntity groupEntity) {
        if (groupEntity.getId() != null && findGroupById(groupEntity.getId()) != null) {
            // TODO: Create ErrorMessage class and use it in the response body
            return new ResponseEntity(getResponseHeaders(), HttpStatus.CONFLICT);
        }

        getGroupService().save(groupEntity);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
    }


    /**
     *
     * @param groupId
     * @param groupEntity
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}", method = PUT)
    public ResponseEntity saveGroup(@PathVariable String groupId, @RequestBody GroupEntity groupEntity) {
        if (findGroupById(groupId) != null) {
            groupEntity.setId(groupId);
            getGroupService().save(groupEntity);
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}", method = GET)
    public ResponseEntity getGroup(@PathVariable String groupId) {
        GroupEntity groupEntity = findGroupById(groupId);

        if (groupEntity != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return new ResponseEntity(groupEntity, getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}/request-list", method = GET)
    public ResponseEntity getRequestList(@PathVariable String groupId) {
        GroupEntity groupEntity = findGroupById(groupId);

        if (groupEntity != null) {
            List<IRequestEntity> requestList = requestService.findAllByGroupId(groupEntity.getId());

            if (requestList == null) {
                requestList = new ArrayList<IRequestEntity>();
            }

            BootGridRowMapper mapper = new BootGridRowMapper();
            BootGridDTO bootGrid = new BootGridDTO();
            bootGrid.setCurrent(1);
            bootGrid.setTotal(3);
            bootGrid.setRowCount(3);
            bootGrid.setRows(mapper.map(requestList));

            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return new ResponseEntity(bootGrid, getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}", method = DELETE)
    public ResponseEntity deleteGroup(@PathVariable String groupId) {
        if (findGroupById(groupId) != null) {
            getGroupService().remove(groupId);
            return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }
}
