package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.EventEntityType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

import static com.socialstartup.mockenger.core.util.HttpUtils.getListOfHeaders;
import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;


/**
 * @author Dmitry Ryazanov
 */
@RestController
@RequestMapping(path = API_PATH + "/valueset")
public class ValueSetController extends AbstractController {

    @Autowired
    private ProjectService projectService;


    /**
     * Returns all the values for ProjectType
     *
     * @return 200 OK or 404 NOT FOUND
     */
    @GetMapping("/projectTypes")
    public ResponseEntity getProjectTypes() {
        return okResponseWithDefaultHeaders(asList(ProjectType.values()));
    }



	/**
     * Returns list of all the request methods or filtered list by provided projectId
     *
     * @param projectId projectId
     * @return 200 OK or 404 NOT FOUND
     */
	@GetMapping("/requestMethods")
    public ResponseEntity getRequestMethods(@RequestParam(value = "projectId", required = false) final String projectId) {
        if (StringUtils.isEmpty(projectId)) {
            return okResponseWithDefaultHeaders(asList(RequestMethod.values()));
        } else {
            final Project project = projectService.findById(projectId);

            if (project != null && project.getType() != null) {
				return okResponseWithDefaultHeaders(project.getType().getAllowedMethods());
            }
        }

        return notFoundWithDefaultHeaders();
    }


	/**
     * Returns all the values for TransformerType
     *
     * @return 200 OK
     */
    @GetMapping("/transformerTypes")
    public ResponseEntity getTransformerTypes() {
        return okResponseWithDefaultHeaders(asList(TransformerType.values()));
    }


    /**
     * Returns all the values for TransformerType
     *
     * @return 200 OK
     */
    @GetMapping("/headers")
    public ResponseEntity getHeaders() {
        return okResponseWithDefaultHeaders(getListOfHeaders());
    }


    /**
     * Returns all the roles
     *
     * @return 200 OK
     */
    @GetMapping("/roles")
    public ResponseEntity getRoles() {
        return okResponseWithDefaultHeaders(RoleType.values());
    }

    /**
     * Returns all the values of EventType
     *
     * @return 200 OK
     */
    @GetMapping("/eventTypes")
    public ResponseEntity getEventTypes() {
        return okResponseWithDefaultHeaders(EventType.values());
    }


	/**
	 * Returns all the values of EventEntityType
	 *
	 * @return 200 OK
	 */
	@GetMapping("/eventEntityTypes")
	public ResponseEntity getEventEntityTypes() {
		final Map<String, String> entityTypes = Arrays.stream(EventEntityType.values())
				.collect(toMap(e -> e.name(), e -> e.getTypeName()));

		return okResponseWithDefaultHeaders(ImmutableList.of(entityTypes));
	}
}
