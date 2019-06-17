package ca.tunestumbler.api.exceptions;

public class UserServiceException extends RuntimeException {
	private static final long serialVersionUID = -887082629840764178L;

	public UserServiceException(String message) {
		super(message);
	}
}
