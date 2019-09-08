package ca.tunestumbler.api.exceptions;

public class FiltersNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1218626005839289232L;

	public FiltersNotFoundException() {
		super();
	}

	public FiltersNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FiltersNotFoundException(String message) {
		super(message);
	}

	public FiltersNotFoundException(Throwable cause) {
		super(cause);
	}

}
