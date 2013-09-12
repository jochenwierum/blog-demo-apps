package de.jowisoftware.myDI.exceptions;

public class DIInitializationException extends DIException {
    private static final long serialVersionUID = 8994979534657917898L;

    public DIInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DIInitializationException(final String message) {
        super(message);
    }
}
