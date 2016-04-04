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
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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
    public ResponseEntity getGroupById(@PathVariable final String groupId) {
        return new ResponseEntity(findGroupById(groupId), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param group
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPS, method = POST)
    public ResponseEntity addGroup(@Valid @RequestBody final Group group, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Group groupToAdd = getGroupService().getGroupClone(group).id(null).build();
        return new ResponseEntity(getGroupService().save(groupToAdd), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param groupId
     * @param group
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPID, method = PUT)
    public ResponseEntity saveGroup(@PathVariable final String groupId,
                                    @Valid @RequestBody final Group group,
                                    final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if group exists
        findGroupById(groupId);

        if (!groupId.equals(group.getId())) {
            throw new IllegalArgumentException("Group IDs in the URL and in the payload are not equals");
        }

        return new ResponseEntity(getGroupService().save(group), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPID, method = DELETE)
    public ResponseEntity deleteGroup(@PathVariable final String groupId) {
        getGroupService().remove(findGroupById(groupId));
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = GROUPS, method = GET)
    public ResponseEntity getGroupList(@PathVariable final String projectId) {
        final Project project = findProjectById(projectId);
        final List<Group> groupList = getGroupService().findByProjectId(project.getId());

        return new ResponseEntity(groupList, getResponseHeaders(), HttpStatus.OK);
    }
}
