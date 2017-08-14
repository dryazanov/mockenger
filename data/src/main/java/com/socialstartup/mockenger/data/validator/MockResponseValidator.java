package com.socialstartup.mockenger.data.validator;

import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dmitry Ryazanov on 7/6/2015.
 */
public class MockResponseValidator implements ConstraintValidator<MockResponseValidation, MockResponse> {

    private final static String MSG = "httpStatus: must be number greater than zero";

    @Override
    public void initialize(MockResponseValidation mockResponseValidation) {
        // nothing to do
    }

    @Override
    public boolean isValid(MockResponse mockResponse, ConstraintValidatorContext context) {
        if (mockResponse != null && mockResponse.getHttpStatus() <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MSG).addConstraintViolation();
            return false;
        }
        return true;
    }
}
