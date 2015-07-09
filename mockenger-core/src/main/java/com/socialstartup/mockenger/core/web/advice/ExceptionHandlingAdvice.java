package com.socialstartup.mockenger.core.web.advice;

import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.core.web.exception.ObjectAlreadyExistsException;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    /**
     * Handle errors about bad content-type in the header
     *
     * @param ex
     * @return error message
     */
    @ResponseBody
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorMessage handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        LOG.debug("ObjectAlreadyExistsException has occurred", ex);
        return new ErrorMessage("Object already exists");
    }

    /**
     * Handle errors when a record of project, group or request not found
     *
     * @param ex
     * @return error message
     */
    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorMessage handleObjectNotFoundException(ObjectNotFoundException ex) {
        String className = "";
        String template = "%s with ID '%s' not found";
        if (ex.getClassName() != null) {
            className = ex.getClassName();
        } else {
            className = "Item";
        }
        String message = String.format(template, className, ex.getItemId());
        LOG.debug(message, ex);
        return new ErrorMessage(message);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOG.error("JSON is not readable", ex);
        return new ErrorMessage("Unable to process request: json is not readable");
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
        return new ErrorMessage((ex == null || ex.getMessage() == null) ? "Unable to process request" : ex.getMessage());
    }
}
