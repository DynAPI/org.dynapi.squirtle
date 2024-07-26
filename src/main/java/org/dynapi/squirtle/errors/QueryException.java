package org.dynapi.squirtle.errors;

public class QueryException extends SquirtleError {
    public QueryException() {
        super();
    }
    public QueryException(String message) {
        super(message);
    }
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
    public QueryException(Throwable cause) {
        super(cause);
    }
}
