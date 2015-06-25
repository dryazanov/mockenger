package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.mapper.request.GridRowMapper;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dto.Grid;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
@RequestMapping(value = {"/groups"})
public class GroupController extends AbstractController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);

    /**
     *
     * @param group
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = POST)
    public ResponseEntity addGroup(@RequestBody Group group) {
        if (group.getId() != null && findGroupById(group.getId()) != null) {
            // TODO: Create ErrorMessage class and use it in the response body
            return new ResponseEntity(getResponseHeaders(), HttpStatus.CONFLICT);
        }

        getGroupService().save(group);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
    }


    /**
     *
     * @param profile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}", method = PUT)
    public ResponseEntity saveGroup(@PathVariable String groupId, @RequestBody Group profile) {
        if (findGroupById(groupId) != null) {
            getGroupService().save(profile);
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
        Group profile = findGroupById(groupId);

        if (profile != null) {
            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return new ResponseEntity(profile, getResponseHeaders(), HttpStatus.OK);
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
        Group profile = findGroupById(groupId);
        if (profile != null) {
            getGroupService().remove(profile);
            return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }


//    @ResponseBody
//    @RequestMapping(value = {"", "/"}, method = GET)
//    public ResponseEntity getGroupList(@RequestParam(value = "type") GroupType type) {
//        if (type != null) {
//            List<Group> groupList = getGroupService().findByType(type);
//
//            if (groupList == null) {
//                groupList = new ArrayList<>();
//            }
//
//            Grid bootGrid = new Grid();
//            bootGrid.setCurrent(1);
//            bootGrid.setTotal(3);
//            bootGrid.setRowCount(3);
//            bootGrid.setRows(groupList);
//
//            getResponseHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//            return new ResponseEntity(bootGrid, getResponseHeaders(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
//        }
//    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{groupId}/requests", method = GET)
    public ResponseEntity getRequestList(@PathVariable String groupId) {
        Group group = findGroupById(groupId);

        if (group != null) {
            List<AbstractRequest> requestList = getRequestService().findByGroupId(group.getId());

            if (requestList == null) {
                requestList = new ArrayList<>();
            }

            GridRowMapper mapper = new GridRowMapper();
            Grid bootGrid = new Grid();
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
}
