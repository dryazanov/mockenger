package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class ProjectController extends AbstractController {

    /**
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = PROJECT_ID_ENDPOINT, method = GET)
    public ResponseEntity getProject(@PathVariable final String projectId) {
        return new ResponseEntity(findProjectById(projectId), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = PROJECTS_ENDPOINT, method = GET)
    public ResponseEntity getProjectList() {
        return new ResponseEntity(getProjectService().findAll(), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param project
     * @param result
     * @return
     */
    @RequestMapping(value = PROJECTS_ENDPOINT, method = POST)
    public ResponseEntity addProject(@Valid @RequestBody final Project project, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Project projectToAdd = ProjectService.getProjectClone(project).id(null).build();
        return new ResponseEntity(getProjectService().save(projectToAdd), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param projectId
     * @param project
     * @param result
     * @return
     */
    @RequestMapping(value = PROJECT_ID_ENDPOINT, method = PUT)
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
    @RequestMapping(value = PROJECT_ID_ENDPOINT, method = DELETE)
    public ResponseEntity deleteProject(@PathVariable final String projectId) {
        getProjectService().remove(findProjectById(projectId));
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }
}
