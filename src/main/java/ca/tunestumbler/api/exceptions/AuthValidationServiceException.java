package ca.tunestumbler.api.exceptions;

public class AuthValidationServiceException extends RuntimeException {
	private static final long serialVersionUID = 1806963539755752449L;

	public AuthValidationServiceException(String message) {
		super(message);
	}

}
