package ca.tunestumbler.api.exceptions;

public class MissingPathParametersException extends RuntimeException {
	private static final long serialVersionUID = 7455461200038528292L;

	public MissingPathParametersException() {
		super();
	}

	public MissingPathParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingPathParametersException(String message) {
		super(message);
	}

	public MissingPathParametersException(Throwable cause) {
		super(cause);
	}

}
