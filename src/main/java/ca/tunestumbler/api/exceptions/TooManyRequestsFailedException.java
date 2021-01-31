package ca.tunestumbler.api.exceptions;

public class TooManyRequestsFailedException extends RuntimeException {
	private static final long serialVersionUID = 1516260373325789900L;

	public TooManyRequestsFailedException() {
		super();
	}

	public TooManyRequestsFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooManyRequestsFailedException(String message) {
		super(message);
	}

	public TooManyRequestsFailedException(Throwable cause) {
		super(cause);
	}

}
