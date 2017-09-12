package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.core.service.ProjectService;
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
import java.net.URI;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class ProjectController extends AbstractController {

	/**
	 *
	 * @param projectCode
	 * @return
	 */
	@GetMapping(PROJECT_CODE_ENDPOINT)
	public ResponseEntity getProjectByCode(@PathVariable final String projectCode) {
		return okResponseWithDefaultHeaders(findProjectByCode(projectCode));
	}


    /**
     *
     * @return
     */
    @GetMapping(PROJECTS_ENDPOINT)
    public ResponseEntity getProjectList() {
        return okResponseWithDefaultHeaders(projectService.findAll());
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

        final Project projectToAdd = ProjectService.cloneProject(project).id(null).build();
		final URI uri = URI.create(API_PATH + "/projects/" + project.getCode());

		return createdResponseWithDefaultHeaders(uri, projectService.save(projectToAdd));
    }


    /**
     *
     * @param projectCode
     * @param project
     * @param result
     * @return
     */
    @PutMapping(PROJECT_CODE_ENDPOINT)
    public ResponseEntity saveProject(@PathVariable final String projectCode,
                                      @Valid @RequestBody final Project project,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if project exists
        final Project existingProject = findProjectByCode(projectCode);

        if (!existingProject.getId().equals(project.getId())) {
            throw new IllegalArgumentException("Project IDs in the URL and in the payload are not equals");
        }

        return okResponseWithDefaultHeaders(projectService.save(project));
    }


    /**
     *
     * @param projectCode
     * @return
     */
    @DeleteMapping(PROJECT_CODE_ENDPOINT)
    public ResponseEntity deleteProject(@PathVariable final String projectCode) {
        projectService.remove(findProjectByCode(projectCode));

        return noContentWithDefaultHeaders();
    }
}
