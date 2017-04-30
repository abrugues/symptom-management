package org.coursera.capstone.client;

/**
 * A special class made to specify exceptions that are thrown by our
 * SecuredRestBuilder.
 * 
 * A more robust implementation would probably have fields for tracking
 * the type of exception (e.g., bad password, etc.).
 * 
 * @author jules
 *
 */
public class SecuredRestException extends RuntimeException {

	public SecuredRestException() {
		super();
	}
	
	public SecuredRestException(String detailMessage) {
		super(detailMessage);
	}
	
	public SecuredRestException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
	
	public SecuredRestException(Throwable throwable) {
		super(throwable);
	}
}
