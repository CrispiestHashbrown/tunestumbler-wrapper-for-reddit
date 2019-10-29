package ca.tunestumbler.api.exceptions;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = -6323668037253969966L;

	public BadRequestException() {
		super();
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}

}
