package org.dynapi.squirtle.errors;

public class SquirtleError extends RuntimeException {
    public SquirtleError() {
        super();
    }
    public SquirtleError(String message) {
        super(message);
    }
    public SquirtleError(String message, Throwable cause) {
        super(message, cause);
    }
    public SquirtleError(Throwable cause) {
        super(cause);
    }
}
