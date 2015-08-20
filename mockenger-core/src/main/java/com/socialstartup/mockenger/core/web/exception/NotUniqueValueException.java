package com.socialstartup.mockenger.core.web.exception;

import org.springframework.dao.DuplicateKeyException;

/**
 * Created by x079089 on 6/19/2015.
 */
public class NotUniqueValueException extends DuplicateKeyException {

    public NotUniqueValueException(String msg) {
        super(msg);
    }
}
