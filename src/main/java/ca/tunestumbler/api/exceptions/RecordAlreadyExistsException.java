package ca.tunestumbler.api.exceptions;

public class RecordAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -887082629840764178L;

	public RecordAlreadyExistsException() {
		super();
	}

	public RecordAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecordAlreadyExistsException(String message) {
		super(message);
	}

	public RecordAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
