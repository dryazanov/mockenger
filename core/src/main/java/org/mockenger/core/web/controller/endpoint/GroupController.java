package org.mockenger.core.web.controller.endpoint;

import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
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

import static org.mockenger.core.service.GroupService.cloneGroup;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class GroupController extends AbstractController {

    /**
     *
     * @param groupCode
     * @return
     */
    @GetMapping(GROUP_CODE_ENDPOINT)
    public ResponseEntity getGroupByCode(@PathVariable final String groupCode) {
        return okResponseWithDefaultHeaders(findGroupByCode(groupCode));
    }


    /**
     *
     * @param group
     * @param result
     * @return
     */
    @PostMapping(GROUPS_ENDPOINT)
    public ResponseEntity addGroup(@Valid @RequestBody final Group group, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

		final Project project = findProjectById(group.getProjectId());
        final Group groupCandidate = cloneGroup(group).id(null).build();
		final URI uri = URI.create(API_PATH + "/projects/" + project.getCode() + "/groups/" + group.getCode());

        return createdResponseWithDefaultHeaders(uri, groupService.save(groupCandidate));
    }


    /**
     *
     * @param groupCode
     * @param group
     * @return
     */
    @PutMapping(GROUP_CODE_ENDPOINT)
    public ResponseEntity saveGroup(@PathVariable final String groupCode,
                                    @Valid @RequestBody final Group group,
                                    final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if group exists
        final Group existingGroup = findGroupByCode(groupCode);
		final Group groupCandidate = cloneGroup(group)
				.code(existingGroup.getCode())
				.build();

        if (!existingGroup.getId().equals(group.getId())) {
            throw new IllegalArgumentException("Group IDs in the URL and in the payload are not equals");
        }

        return okResponseWithDefaultHeaders(groupService.save(groupCandidate));
    }


    /**
     *
     * @param groupCode
     * @return
     */
    @DeleteMapping(GROUP_CODE_ENDPOINT)
    public ResponseEntity deleteGroup(@PathVariable final String groupCode) {
        groupService.remove(findGroupByCode(groupCode));

        return noContentWithDefaultHeaders();
    }


    /**
     *
     * @param projectCode
     * @return
     */
	@GetMapping(GROUPS_ENDPOINT)
    public ResponseEntity getGroupList(@PathVariable final String projectCode) {
        final Project project = findProjectByCode(projectCode);
        final Iterable<Group> groupList = groupService.findByProjectId(project.getId());

        return okResponseWithDefaultHeaders(groupList);
    }
}
