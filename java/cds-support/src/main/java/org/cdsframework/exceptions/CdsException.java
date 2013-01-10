package org.cdsframework.exceptions;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsException extends Exception {

    /**
     * Creates a new instance of <code>CdsException</code> without detail message.
     */
    public CdsException() {
    }

    /**
     * Constructs an instance of <code>CdsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CdsException(String msg) {
        super(msg);
    }
}
