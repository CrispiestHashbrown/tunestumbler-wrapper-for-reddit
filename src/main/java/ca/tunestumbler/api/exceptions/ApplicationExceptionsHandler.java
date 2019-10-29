package ca.tunestumbler.api.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import ca.tunestumbler.api.ui.model.response.ErrorMessage;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@ControllerAdvice
public class ApplicationExceptionsHandler {

	@ExceptionHandler(value = { MissingPathParametersException.class })
	public ResponseEntity<Object> handleMissingPathParameterException(MissingPathParametersException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "MISSING PARAMETER: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { InvalidBodyException.class })
	public ResponseEntity<Object> handleInvalidBodyException(InvalidBodyException exception, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "INVALID REQUEST BODY: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { RecordAlreadyExistsException.class })
	public ResponseEntity<Object> handleRecordAlreadyExistsException(RecordAlreadyExistsException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "RECORD ALREADY EXISTS: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { AuthValidationServiceException.class })
	public ResponseEntity<Object> handleAuthValidationServiceException(AuthValidationServiceException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { RedditAccountNotAuthenticatedException.class })
	public ResponseEntity<Object> handleAuthValidationServiceException(RedditAccountNotAuthenticatedException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = { BadRequestException.class })
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
		return new ResponseEntity<>(ErrorPrefixes.USER_ERROR.getErrorPrefix() + ErrorMessages.BAD_REQUEST.getErrorMessage(), 
				new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "RESOURCE NOT FOUND: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { SubredditsNotFoundException.class })
	public ResponseEntity<Object> handleSubredditsNotFoundException(SubredditsNotFoundException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(),
				"NO SUBREDDITS SUBSCRIBED OR CURATED: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { FiltersNotFoundException.class })
	public ResponseEntity<Object> handleFiltersNotFoundException(FiltersNotFoundException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "NO FILTERS FOUND: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { WebRequestFailedException.class })
	public ResponseEntity<Object> handleWebRequestFailedException(WebRequestFailedException exception,
			WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "WEB REQUEST FAILED: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherExceptions(Exception exception, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), "CATCHALL EXCEPTION: " + exception.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
