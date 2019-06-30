package ca.tunestumbler.api.exceptions;

public class AggregateServiceException extends RuntimeException {
	private static final long serialVersionUID = 227971404516445016L;

	public AggregateServiceException(String message) {
		super(message);
	}

}
