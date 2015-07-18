package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
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
 * Created by x079089 on 3/24/2015.
 */
@Controller
public class GroupController extends AbstractController {

    private static final String GROUPS = PROJECT_ID_ENDPOINT + GROUPS_ENDPOINT;
    private static final String GROUPID = PROJECT_ID_ENDPOINT + GROUP_ID_ENDPOINT;


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPID, method = GET)
    public ResponseEntity getGroup(@PathVariable String groupId) {
        Group group = findGroupById(groupId);
        return new ResponseEntity(group, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param group
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPS, method = POST)
    public ResponseEntity addGroup(@Valid @RequestBody Group group, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        group.setId(null);
        getGroupService().save(group);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
    }


    /**
     *
     * @param groupId
     * @param group
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPID, method = PUT)
    public ResponseEntity saveGroup(@PathVariable String groupId, @Valid @RequestBody Group group, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        findGroupById(groupId); // Check if group exists
        getGroupService().save(group);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPID, method = DELETE)
    public ResponseEntity deleteGroup(@PathVariable String groupId) {
        Group group = findGroupById(groupId);
        getGroupService().remove(group);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPS, method = GET)
    public ResponseEntity getGroupList(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        List<Group> groupList = getGroupService().findByProjectId(project.getId());
        return new ResponseEntity((groupList != null ? groupList : new ArrayList<>()), getResponseHeaders(), HttpStatus.OK);
    }
}
