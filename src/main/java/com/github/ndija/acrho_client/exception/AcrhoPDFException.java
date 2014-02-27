package com.github.ndija.acrho_client.exception;


public class AcrhoPDFException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -8924237858892047621L;

    public AcrhoPDFException(String message, Throwable e) {
        super(message, e);
    }

    public AcrhoPDFException(String message) {
        super(message);
    }
}
