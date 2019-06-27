package ca.tunestumbler.api.exceptions;

public class SubredditServiceException extends RuntimeException {
	private static final long serialVersionUID = 1566180386078732826L;

	public SubredditServiceException(String message) {
		super(message);
	}

}
