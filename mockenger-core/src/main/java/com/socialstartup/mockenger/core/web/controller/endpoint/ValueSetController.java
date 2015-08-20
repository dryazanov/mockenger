package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.dict.TransformerType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by x079089 on 3/24/2015.
 */
@Controller
public class ValueSetController extends AbstractController {

    private final String PROJECT_TYPE = "project-type";
    private final String REQUEST_METHOD = "request-method";
    private final String TRANSFORMER_TYPE = "transformer-type";

    /**
     * Returns valueset by provided id
     *
     * @param id valueset id
     * @return 200 OK or 404 NOT FOUND
     */
    @ResponseBody
    @RequestMapping(value = VALUESET_ENDPOINT, method = GET)
    public ResponseEntity getValueSet(@RequestParam(value = "id", required = true) String id) {
        Map<String, String> valueset = null;
        if (PROJECT_TYPE.equals(id)) {
            valueset = ProjectType.getValueSet();
        } else if (REQUEST_METHOD.equals(id)) {
            valueset = RequestMethod.getValueSet();
        } else if (TRANSFORMER_TYPE.equals(id)) {
            valueset = TransformerType.getValueSet();
        }

        if (valueset != null) {
            return new ResponseEntity(valueset, getResponseHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity(getResponseHeaders(), HttpStatus.NOT_FOUND);
        }
    }
}
