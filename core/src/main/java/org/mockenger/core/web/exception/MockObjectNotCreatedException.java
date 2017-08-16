package org.mockenger.core.web.exception;

/**
 * Created by Dmitry Ryazanov on 6/19/2015.
 */
public class MockObjectNotCreatedException extends RuntimeException {
    public MockObjectNotCreatedException() {
        super();
    }

    public MockObjectNotCreatedException(String message) {
        super(message);
    }

    public MockObjectNotCreatedException(Throwable ex) {
        super(ex);
    }

    public MockObjectNotCreatedException(String message, Throwable ex) {
        super(message, ex);
    }
}
