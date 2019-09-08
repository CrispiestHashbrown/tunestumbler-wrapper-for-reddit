package ca.tunestumbler.api.exceptions;

public class WebRequestFailedException extends RuntimeException {
	private static final long serialVersionUID = 6896757968361640590L;

	public WebRequestFailedException() {
		super();
	}

	public WebRequestFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebRequestFailedException(String message) {
		super(message);
	}

	public WebRequestFailedException(Throwable cause) {
		super(cause);
	}

}
