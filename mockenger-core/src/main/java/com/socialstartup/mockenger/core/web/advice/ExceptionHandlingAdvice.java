package com.socialstartup.mockenger.core.web.advice;

import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.data.model.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dryazanov
 */
@ControllerAdvice
public class ExceptionHandlingAdvice {

    Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    /**
     * Handle errors about bad content-type in the header
     *
     * @param ex
     * @return error message
     */
    @ResponseBody
    @ExceptionHandler(BadContentTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleBadContentTypeException(BadContentTypeException ex) {
        LOG.debug("BadContentTypeException has occurred", ex);
        return new ErrorMessage(ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorMessage handleError500(HttpServletRequest req, RuntimeException ex) {
        LOG.error("RuntimeException has occurred", ex);
        return new ErrorMessage("Unable to process request. Internal server error: " + ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException ex) {
        LOG.error("IllegalArgumentException has occurred", ex);
        return new ErrorMessage("Unable to process request. " + ex.getMessage());
    }
}
