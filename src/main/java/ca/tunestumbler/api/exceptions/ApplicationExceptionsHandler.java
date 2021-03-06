package ca.tunestumbler.api.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.ui.model.response.ErrorObject;
import ca.tunestumbler.api.ui.model.response.ErrorsResponse;

@ControllerAdvice
public class ApplicationExceptionsHandler {

	@Autowired
	SharedUtils sharedUtils;

	@ExceptionHandler(value = { MissingPathParametersException.class })
	public ResponseEntity<Object> handleMissingPathParameterException(MissingPathParametersException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"MISSING PARAMETER",
				exception.getMessage(), 
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { InvalidBodyException.class })
	public ResponseEntity<Object> handleInvalidBodyException(InvalidBodyException exception, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"INVALID REQUEST BODY",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { RecordAlreadyExistsException.class })
	public ResponseEntity<Object> handleRecordAlreadyExistsException(RecordAlreadyExistsException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"RECORD ALREADY EXISTS",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { AuthValidationServiceException.class })
	public ResponseEntity<Object> handleAuthValidationServiceException(AuthValidationServiceException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"BAD REQUEST",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { RedditAccountNotAuthenticatedException.class })
	public ResponseEntity<Object> handleRedditAccountNotAuthenticatedException(RedditAccountNotAuthenticatedException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"UNAUTHORIZED",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}
	
	@ExceptionHandler(value = { BadRequestException.class })
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"USER ERROR",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"RESOURCE NOT FOUND",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { SubredditsNotFoundException.class })
	public ResponseEntity<Object> handleSubredditsNotFoundException(SubredditsNotFoundException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"NO SUBREDDITS SUBSCRIBED OR CURATED",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { FiltersNotFoundException.class })
	public ResponseEntity<Object> handleFiltersNotFoundException(FiltersNotFoundException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"NO FILTERS FOUND",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { NoResultsFoundForNonexistingSubredditException.class })
	public ResponseEntity<Object> handleNoResultsFoundForNonexistingSubredditException(
			NoResultsFoundForNonexistingSubredditException exception, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"NO RESULTS FOUND. SUBREDDIT(S) MAY NOT EXIST",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { TooManyRequestsFailedException.class })
	public ResponseEntity<Object> handleTooManyRequestsFailedException(TooManyRequestsFailedException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"TOO MANY REDDIT REQUESTS",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}
	
	@ExceptionHandler(value = { WebRequestFailedException.class })
	public ResponseEntity<Object> handleWebRequestFailedException(WebRequestFailedException exception,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"WEB REQUEST FAILED",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherExceptions(Exception exception, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorObject errorObject = new ErrorObject(
				httpStatus.toString(),
				"INTERNAL EXCEPTION",
				exception.getMessage(),
				sharedUtils.getCurrentTime());

		return new ResponseEntity<>(createErrorsResponse(errorObject), httpStatus);
	}

	private ErrorsResponse createErrorsResponse(ErrorObject errorObject) {
		List<ErrorObject> errors = new ArrayList<>();
		errors.add(errorObject);
		ErrorsResponse errorResponse = new ErrorsResponse();
		errorResponse.setErrors(errors);
		return errorResponse;
	}
	
}
