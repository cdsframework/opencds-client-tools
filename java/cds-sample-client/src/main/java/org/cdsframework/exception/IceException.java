package org.cdsframework.exception;

/**
 *
 * @author HLN Consulting, LLC
 */
public class IceException extends Exception {

    /**
     * Creates a new instance of <code>IceException</code> without detail message.
     */
    public IceException() {
    }

    /**
     * Constructs an instance of <code>IceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IceException(String msg) {
        super(msg);
    }
}
