package ca.tunestumbler.api.exceptions;

public class RedditAccountNotAuthenticatedException extends RuntimeException {
	private static final long serialVersionUID = -7814837418133089264L;

	public RedditAccountNotAuthenticatedException() {
		super();
	}

	public RedditAccountNotAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedditAccountNotAuthenticatedException(String message) {
		super(message);
	}

	public RedditAccountNotAuthenticatedException(Throwable cause) {
		super(cause);
	}

}
