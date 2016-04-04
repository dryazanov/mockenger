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

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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
    public ResponseEntity getProject(@PathVariable final String projectId) {
        return new ResponseEntity(findProjectById(projectId), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTS, method = GET)
    public ResponseEntity getProjectList() {
        return new ResponseEntity(getProjectService().findAll(), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param project
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTS, method = POST)
    public ResponseEntity addProject(@Valid @RequestBody final Project project, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Project projectToAdd = getProjectService().getProjectClone(project).id(null).build();
        return new ResponseEntity(getProjectService().save(projectToAdd), getResponseHeaders(), HttpStatus.OK);
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
    public ResponseEntity saveProject(@PathVariable final String projectId,
                                      @Valid @RequestBody final Project project,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if project exists
        findProjectById(projectId);

        if (!projectId.equals(project.getId())) {
            throw new IllegalArgumentException("Project IDs in the URL and in the payload are not equals");
        }

        return new ResponseEntity(getProjectService().save(project), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param projectId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PROJECTID, method = DELETE)
    public ResponseEntity deleteProject(@PathVariable final String projectId) {
        getProjectService().remove(findProjectById(projectId));
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }
}
