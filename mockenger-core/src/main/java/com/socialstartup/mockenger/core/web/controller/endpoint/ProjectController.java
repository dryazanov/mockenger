package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.socialstartup.mockenger.core.service.ProjectService.cloneProject;

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
    @GetMapping(PROJECT_ID_ENDPOINT)
    public ResponseEntity getProject(@PathVariable final String projectId) {
        return okResponseWithDefaultHeaders(findProjectById(projectId));
    }


    /**
     *
     * @return
     */
    @GetMapping(PROJECTS_ENDPOINT)
    public ResponseEntity getProjectList() {
        return okResponseWithDefaultHeaders(getProjectService().findAll());
    }


    /**
     *
     * @param project
     * @param result
     * @return
     */
    @PostMapping(PROJECTS_ENDPOINT)
    public ResponseEntity addProject(@Valid @RequestBody final Project project, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Project projectToAdd = cloneProject(project).id(null).build();

        return okResponseWithDefaultHeaders(getProjectService().save(projectToAdd));
    }


    /**
     *
     * @param projectId
     * @param project
     * @param result
     * @return
     */
    @PutMapping(PROJECT_ID_ENDPOINT)
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

        return okResponseWithDefaultHeaders(getProjectService().save(project));
    }


    /**
     *
     * @param projectId
     * @return
     */
    @DeleteMapping(PROJECT_ID_ENDPOINT)
    public ResponseEntity deleteProject(@PathVariable final String projectId) {
        getProjectService().remove(findProjectById(projectId));

        return noContentWithDefaultHeaders();
    }
}
