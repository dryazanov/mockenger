package org.mockenger.core.web.controller.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Provides configuration constants for the frontend as a javascript file
 *
 * @author Dmitry Ryazanov
 */
@Slf4j
@RestController
@RequestMapping(path = "/modules/components/constants.js")
public class ConstantsController {

	@Autowired
	@Qualifier("constantsBean")
	private String constants;


    /**
     *
     * @return
     */
    @GetMapping(produces = "text/javascript")
    public ResponseEntity getConstants() {
        return ResponseEntity.ok(constants);
    }
}
