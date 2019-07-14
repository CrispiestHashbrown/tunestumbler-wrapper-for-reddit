package ca.tunestumbler.api.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -7192670267544448318L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
