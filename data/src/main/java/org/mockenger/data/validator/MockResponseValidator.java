package org.mockenger.data.validator;

import org.mockenger.data.model.persistent.mock.response.MockResponse;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Dmitry Ryazanov
 */
public class MockResponseValidator implements ConstraintValidator<MockResponseValidation, MockResponse> {

    private final static String MSG = "httpStatus: must be number greater than zero";

    @Override
    public void initialize(final MockResponseValidation mockResponseValidation) {
        // nothing to do
    }

    @Override
    public boolean isValid(final MockResponse mockResponse, final ConstraintValidatorContext context) {
        if (mockResponse != null && mockResponse.getHttpStatus() <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MSG).addConstraintViolation();
            return false;
        }

        return true;
    }
}
