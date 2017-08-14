package org.mockenger.data.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dmitry Ryazanov on 7/6/2015.
 */
@Constraint(validatedBy = MockResponseValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MockResponseValidation {

    String message() default "Mock-response has incorrect structure or contains invalid data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}