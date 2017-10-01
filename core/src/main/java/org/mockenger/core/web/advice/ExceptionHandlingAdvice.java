package org.mockenger.core.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.mockenger.core.web.exception.AccountDeleteException;
import org.mockenger.core.web.exception.BadContentTypeException;
import org.mockenger.core.web.exception.MockObjectNotCreatedException;
import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.core.web.exception.ObjectNotFoundException;
import org.mockenger.data.model.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlingAdvice {

    /**
     * Handle errors about bad content-type in the header
     *
     * @param ex
     * @return error message
     */
    @ExceptionHandler(BadContentTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleBadContentTypeException(final BadContentTypeException ex) {
        log.debug("BadContentTypeException has occurred", ex);

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
        log.debug("NotUniqueValueException has occurred", ex);
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

        log.debug(sb.toString(), ex);

        return new ErrorMessage(sb.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        log.error("Object is not readable", ex);

        return new ErrorMessage("Unable to process: request object is not readable");
    }

    @ExceptionHandler(AccountDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleAccountDeleteException(final AccountDeleteException ex) {
        log.error("", ex);

        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(MockObjectNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    public ErrorMessage handleMockObjectNotCreatedException(final MockObjectNotCreatedException ex) {
        final String msg = "Failed to create instance of the mock-object";
        log.error(msg, ex);

        return new ErrorMessage(String.format("%s: %s", msg, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorMessage handleIllegalArgumentException(final IllegalArgumentException ex) {
		String message;
        log.error("IllegalArgumentException has occurred", ex);

		if (ex == null || ex.getMessage() == null) {
			message = "Unable to process request";
		} else {
        	if (ex.getMessage().contains("org.mockenger")) {
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
        log.error("RuntimeException has occurred", ex);

        return getInternalServerErrorMessage();
    }

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
	public ErrorMessage handleError500(final Throwable ex) {
		log.error("Throwable has occurred", ex);

		return getInternalServerErrorMessage();
	}


	private ErrorMessage getInternalServerErrorMessage() {
		return new ErrorMessage("Internal server error: unable to process request");
	}
}
