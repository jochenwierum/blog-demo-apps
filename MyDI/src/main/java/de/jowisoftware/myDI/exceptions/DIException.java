package de.jowisoftware.myDI.exceptions;

public abstract class DIException extends RuntimeException {
    private static final long serialVersionUID = 126005189487545585L;

    public DIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DIException(final String message) {
        super(message);
    }
}
