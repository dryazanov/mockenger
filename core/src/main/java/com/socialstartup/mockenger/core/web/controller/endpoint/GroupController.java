package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
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

import static com.socialstartup.mockenger.core.service.GroupService.cloneGroup;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class GroupController extends AbstractController {

    /**
     *
     * @param groupId
     * @return
     */
    @GetMapping(GROUP_ID_ENDPOINT)
    public ResponseEntity getGroupById(@PathVariable final String groupId) {
        return okResponseWithDefaultHeaders(findGroupById(groupId));
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

        final Group groupToAdd = cloneGroup(group).id(null).build();

        return okResponseWithDefaultHeaders(getGroupService().save(groupToAdd));
    }


    /**
     *
     * @param groupId
     * @param group
     * @return
     */
    @PutMapping(GROUP_ID_ENDPOINT)
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

        return okResponseWithDefaultHeaders(getGroupService().save(group));
    }


    /**
     *
     * @param groupId
     * @return
     */
    @DeleteMapping(GROUP_ID_ENDPOINT)
    public ResponseEntity deleteGroup(@PathVariable final String groupId) {
        getGroupService().remove(findGroupById(groupId));

        return noContentWithDefaultHeaders();
    }


    /**
     *
     * @param projectId
     * @return
     */
	@GetMapping(GROUPS_ENDPOINT)
    public ResponseEntity getGroupList(@PathVariable final String projectId) {
        final Project project = findProjectById(projectId);
        final Iterable<Group> groupList = getGroupService().findByProjectId(project.getId());

        return okResponseWithDefaultHeaders(groupList);
    }
}
