package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
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

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
public class ProjectController extends AbstractController {

    private static final String PROJECTS = PROJECTS_ENDPOINT;
    private static final String PROJECTID = PROJECT_ID_ENDPOINT;


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTID, method = GET)
    public ResponseEntity getProject(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        return new ResponseEntity(project, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTS, method = GET)
    public ResponseEntity getProjectList() {
        Iterable<Project> projectList = getProjectService().findAll();
        return new ResponseEntity((projectList != null ? projectList : new ArrayList<>()), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param project
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTS, method = POST)
    public ResponseEntity addProject(@Valid @RequestBody Project project, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        project.setId(null);
        getProjectService().save(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.CREATED);
    }


    /**
     *
     * @param projectId
     * @param project
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTID, method = PUT)
    public ResponseEntity saveProject(@PathVariable String projectId, @Valid @RequestBody Project project, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        findProjectById(projectId); // Check if project exists
        getProjectService().save(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTID, method = DELETE)
    public ResponseEntity deleteProject(@PathVariable String projectId) {
        Project project = findProjectById(projectId);
        getProjectService().remove(project);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.OK);
    }
}
