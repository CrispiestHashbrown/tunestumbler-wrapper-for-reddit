package ca.tunestumbler.api.exceptions;

public class InvalidBodyException extends RuntimeException {
	private static final long serialVersionUID = -5409419917686065468L;

	public InvalidBodyException() {
		super();
	}

	public InvalidBodyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBodyException(String message) {
		super(message);
	}

	public InvalidBodyException(Throwable cause) {
		super(cause);
	}

}
