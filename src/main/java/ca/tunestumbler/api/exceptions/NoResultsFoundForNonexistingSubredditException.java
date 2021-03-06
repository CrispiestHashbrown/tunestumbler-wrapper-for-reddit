package ca.tunestumbler.api.exceptions;

public class NoResultsFoundForNonexistingSubredditException extends RuntimeException {
	private static final long serialVersionUID = 6741206555596276312L;

	public NoResultsFoundForNonexistingSubredditException() {
		super();
	}

	public NoResultsFoundForNonexistingSubredditException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoResultsFoundForNonexistingSubredditException(String message) {
		super(message);
	}

	public NoResultsFoundForNonexistingSubredditException(Throwable cause) {
		super(cause);
	}

}
