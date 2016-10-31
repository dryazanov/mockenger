package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.EventEntityType;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Dmitry Ryazanov
 */
@RestController
public class ValueSetController extends AbstractController {

    private final static String PROJECT_TYPES_VALUESET = VALUESET_ENDPOINT + "/projectTypes";
    private final static String REQUEST_METHOD_VALUESET = VALUESET_ENDPOINT + "/requestMethods";
    private final static String TRANSFORMER_TYPES_VALUESET = VALUESET_ENDPOINT + "/transformerTypes";
    private final static String HEADERS_VALUESET = VALUESET_ENDPOINT + "/headers";
    private final static String ROLES_VALUESET = VALUESET_ENDPOINT + "/roles";
    private final static String EVENT_TYPES_VALUESET = VALUESET_ENDPOINT + "/eventTypes";
    private final static String EVENT_ENTITY_TYPES_VALUESET = VALUESET_ENDPOINT + "/eventEntityTypes";

    @Autowired
    private ProjectService projectService;


    /**
     * Returns all the values for ProjectType
     *
     * @return 200 OK or 404 NOT FOUND
     */
    @RequestMapping(value = PROJECT_TYPES_VALUESET, method = GET)
    public ResponseEntity getProjectTypes() {
        return new ResponseEntity(Arrays.asList(ProjectType.values()), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Returns list of all the request methods or filtered list by provided projectId
     *
     * @param projectId projectId
     * @return 200 OK or 404 NOT FOUND
     */
    @RequestMapping(value = REQUEST_METHOD_VALUESET, method = GET)
    public ResponseEntity getRequestMethods(@RequestParam(value = "projectId", required = false) String projectId) {
        List valueset = null;

        if (projectId == null) {
            valueset = Arrays.asList(RequestMethod.values());
        } else {
            final Project project = projectService.findById(projectId);

            if (project != null && project.getType() != null) {
                valueset = project.getType().getAllowedMethods();
            }
        }

        return new ResponseEntity(valueset, getResponseHeaders(), (valueset != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }


    /**
     * Returns all the values for TransformerType
     *
     * @return 200 OK
     */
    @RequestMapping(value = TRANSFORMER_TYPES_VALUESET, method = GET)
    public ResponseEntity getTransformerTypes() {
        return new ResponseEntity(Arrays.asList(TransformerType.values()), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Returns all the values for TransformerType
     *
     * @return 200 OK
     */
    @RequestMapping(value = HEADERS_VALUESET, method = GET)
    public ResponseEntity getHeaders() {
        return new ResponseEntity(HttpUtils.getListOfHeaders(), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     * Returns all the roles
     *
     * @return 200 OK
     */
    @RequestMapping(value = ROLES_VALUESET, method = GET)
    public ResponseEntity getRoles() {
        return new ResponseEntity(RoleType.values(), getResponseHeaders(), HttpStatus.OK);
    }

    /**
     * Returns all the values of EventType
     *
     * @return 200 OK
     */
    @RequestMapping(value = EVENT_TYPES_VALUESET, method = GET)
    public ResponseEntity getEventTypes() {
        return new ResponseEntity(EventType.values(), getResponseHeaders(), HttpStatus.OK);
    }


	/**
	 * Returns all the values of EventEntityType
	 *
	 * @return 200 OK
	 */
	@RequestMapping(value = EVENT_ENTITY_TYPES_VALUESET, method = GET)
	public ResponseEntity getEventEntityTypes() {
		final Map<String, String> entityTypes = Arrays.stream(EventEntityType.values())
				.collect(Collectors.toMap(e -> e.name(), e -> e.getTypeName()));

		return new ResponseEntity(ImmutableList.of(entityTypes), getResponseHeaders(), HttpStatus.OK);
	}
}
