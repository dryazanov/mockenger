package com.socialstartup.mockenger.core.web.advice;

import com.socialstartup.mockenger.core.web.exception.AccountDeleteException;
import com.socialstartup.mockenger.core.web.exception.BadContentTypeException;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Dmitry Ryazanov
 */
@RestControllerAdvice
public class ExceptionHandlingAdvice {

    private final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    /**
     * Handle errors about bad content-type in the header
     *
     * @param ex
     * @return error message
     */
    @ExceptionHandler(BadContentTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleBadContentTypeException(final BadContentTypeException ex) {
        LOG.debug("BadContentTypeException has occurred", ex);

        return new ErrorMessage(ex.getMessage());
    }

    /**
     * Handle errors about not unique value
     *
     * @param ex
     * @return error message
     */
    @ExceptionHandler(NotUniqueValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleNotUniqueKeyException(NotUniqueValueException ex) {
        LOG.debug("NotUniqueValueException has occurred", ex);
        return new ErrorMessage(ex.getMessage());
    }

    /**
     * Handle errors when a record of project, group or request not found
     *
     * @param ex
     * @return error message
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorMessage handleObjectNotFoundException(final ObjectNotFoundException ex) {
        final StringBuilder sb = new StringBuilder()
				.append(ex.getClassName() != null ? ex.getClassName() : "Item")
				.append(" with ID '").append(ex.getItemId()).append("' not found");

        LOG.debug(sb.toString(), ex);

        return new ErrorMessage(sb.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        LOG.error("JSON object is not readable", ex);

        return new ErrorMessage("Unable to process request: json is not readable");
    }

    @ExceptionHandler(AccountDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleAccountDeleteException(final AccountDeleteException ex) {
        LOG.error("", ex);

        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(MockObjectNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleMockObjectNotCreatedException(final MockObjectNotCreatedException ex) {
        final String msg = "Failed to create instance of the mock-object";
        LOG.error(msg, ex);

        return new ErrorMessage(String.format("%s: %s", msg, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleIllegalArgumentException(final IllegalArgumentException ex) {
		String message;
        LOG.error("IllegalArgumentException has occurred", ex);

		if (ex == null || ex.getMessage() == null) {
			message = "Unable to process request";
		} else {
        	if (ex.getMessage().contains("com.socialstartup.mockenger")) {
        		message = "Illegal argument exception";
			} else {
				message = ex.getMessage();
			}
		}

        return new ErrorMessage(message);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorMessage handleError500(final RuntimeException ex) {
        LOG.error("RuntimeException has occurred", ex);

        return getInternalServerErrorMessage();
    }

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
	public ErrorMessage handleError500(final Throwable ex) {
		LOG.error("Throwable has occurred", ex);

		return getInternalServerErrorMessage();
	}


	private ErrorMessage getInternalServerErrorMessage() {
		return new ErrorMessage(String.format("Internal server error: unable to process request"));
	}
}
