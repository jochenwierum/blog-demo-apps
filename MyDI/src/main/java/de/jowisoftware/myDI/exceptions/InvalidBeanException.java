package de.jowisoftware.myDI.exceptions;


public class InvalidBeanException extends DIException {
    private static final long serialVersionUID = -5120810712958740319L;

    public InvalidBeanException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidBeanException(final String message) {
        super(message);
    }
}
