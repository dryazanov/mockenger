package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
public class ValueSetController extends AbstractController {

    private final String PROJECT_TYPES_VALUESET = VALUESET_ENDPOINT + "/projectTypes";
    private final String REQUEST_METHOD_VALUESET = VALUESET_ENDPOINT + "/requestMethods";
    private final String TRANSFORMER_TYPE_VALUESET = VALUESET_ENDPOINT + "/transformerTypes";

    @Autowired
    private ProjectService projectService;

    /**
     * Returns all the values for ProjectType
     *
     * @return 200 OK or 404 NOT FOUND
     */
    @ResponseBody
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
    @ResponseBody
    @RequestMapping(value = REQUEST_METHOD_VALUESET, method = GET)
    public ResponseEntity getRequestMethods(@RequestParam(value = "projectId", required = false) String projectId) {
        List valueset = null;

        if (projectId == null) {
            valueset = Arrays.asList(RequestMethod.values());
        } else {
            Project project = projectService.findById(projectId);
            if (project != null && project.getType() != null) {
                valueset = project.getType().getAllowedMethods();
            }
        }

        if (valueset != null) {
            return new ResponseEntity(valueset, getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Returns all the values for TransformerType
     *
     * @return 200 OK
     */
    @ResponseBody
    @RequestMapping(value = TRANSFORMER_TYPE_VALUESET, method = GET)
    public ResponseEntity getTransformerTypes() {
        return new ResponseEntity(Arrays.asList(TransformerType.values()), getResponseHeaders(), HttpStatus.OK);
    }
}
