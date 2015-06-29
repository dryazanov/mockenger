package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.ObjectAlreadyExistsException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = {"/projects"})
public class ProjectController extends AbstractController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{projectId}", method = GET)
    public ResponseEntity getProject(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        return new ResponseEntity(project, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param project
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"", "/"}, method = POST)
    public ResponseEntity addGroup(@RequestBody Project project) {
        if (project.getId() != null && getProjectService().findById(project.getId()) != null) {
            throw new ObjectAlreadyExistsException();
        }
        getProjectService().save(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
    }


    /**
     *
     * @param project
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{projectId}", method = PUT)
    public ResponseEntity saveGroup(@PathVariable String projectId, @RequestBody Project project) {
        findGroupById(projectId); // Check if project exists
        getProjectService().save(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{projectId}", method = DELETE)
    public ResponseEntity deleteProject(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        getProjectService().remove(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{projectId}/groups", method = GET)
    public ResponseEntity getGroupList(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        List<Group> groupList = getGroupService().findByProjectId(project.getId());
        return new ResponseEntity((groupList != null ? groupList : new ArrayList<>()), getResponseHeaders(), HttpStatus.OK);
    }
}
