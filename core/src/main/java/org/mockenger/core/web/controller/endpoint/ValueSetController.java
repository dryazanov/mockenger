package org.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import org.mockenger.core.service.ProjectService;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.dict.EventEntityType;
import org.mockenger.data.model.dict.EventType;
import org.mockenger.data.model.dict.ProjectType;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.dict.RoleType;
import org.mockenger.data.model.dict.TransformerType;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.mockenger.core.util.HttpUtils.getListOfHeaders;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;


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
		final Map<String, String> entityTypes = Stream.of(EventEntityType.values())
				.collect(toMap(Enum::name, EventEntityType::getTypeName));

		return okResponseWithDefaultHeaders(ImmutableList.of(entityTypes));
	}
}
