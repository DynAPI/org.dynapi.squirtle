package org.dynapi.squirtle.errors;

public class DialectNotSupportedError extends SquirtleError {
    public DialectNotSupportedError() {
        super();
    }
    public DialectNotSupportedError(String message) {
        super(message);
    }
    public DialectNotSupportedError(String message, Throwable cause) {
        super(message, cause);
    }
    public DialectNotSupportedError(Throwable cause) {
        super(cause);
    }
}
