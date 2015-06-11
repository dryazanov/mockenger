package org.mockenger.dev.web;

import javax.servlet.http.HttpServletRequest;

import org.mockenger.dev.model.error.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dryazanov
 */
@ControllerAdvice
public class ExceptionHandlingController {

    Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorMessage handleError500(HttpServletRequest req, RuntimeException ex) {
        LOG.error("RuntimeException has occured", ex);
        return new ErrorMessage("Unable to process request. Internal server error.");
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException ex) {
        LOG.error("IllegalArgumentException has occured", ex);
        return new ErrorMessage("Unable to process request. " + ex.getMessage());
    }
}