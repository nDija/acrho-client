package org.acrho.acrho_client.exception;

public class AcrhoException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1802117267513743102L;

	public AcrhoException(String message, Throwable e) {
        super(message, e);
    }

    public AcrhoException(String message) {
        super(message);
    }
}
