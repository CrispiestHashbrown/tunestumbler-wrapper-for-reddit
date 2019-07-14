package ca.tunestumbler.api.exceptions;

public class SubredditsNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -8827314378672401424L;

	public SubredditsNotFoundException() {
		super();
	}

	public SubredditsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubredditsNotFoundException(String message) {
		super(message);
	}

	public SubredditsNotFoundException(Throwable cause) {
		super(cause);
	}

}
