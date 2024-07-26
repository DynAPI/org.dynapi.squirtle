package org.dynapi.squirtle.errors;

public class CaseException extends SquirtleError {
    public CaseException() {
        super();
    }
    public CaseException(String message) {
        super(message);
    }
    public CaseException(String message, Throwable cause) {
        super(message, cause);
    }
    public CaseException(Throwable cause) {
        super(cause);
    }
}
