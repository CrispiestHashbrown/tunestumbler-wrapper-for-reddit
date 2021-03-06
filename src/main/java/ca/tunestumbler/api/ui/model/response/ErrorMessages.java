package ca.tunestumbler.api.ui.model.response;

public enum ErrorMessages {
	MISSING_REQUIRED_PATH_FIELD("Missing required path field. "),
	INVALID_BODY("Invalid body."),
	REDDIT_ACCOUNT_NOT_AUTHENTICATED("Reddit account not authenticated. Please refresh authentication token. "),
	FAILED_EXTERNAL_WEB_REQUEST("External request has failed. Please try again later. "),
	RECORD_ALREADY_EXISTS("Record already exists. "),
	INTERNAL_SERVER_ERROR("Internal server error. "),
	NO_RECORD_FOUND("Record with provided id is not found. "),
	SUBREDDIT_RESOURCES_NOT_FOUND("Subreddit resources not found. "),
	FILTER_RESOURCES_NOT_FOUND("Filter resources not found. "),
	NO_SEARCH_REULTS_FOUND_FOR_NON_EXISTING_SUBREDDITS("No search results found for subreddit(s) that may not exist. Try double checking subreddit(s) spelling. "),
	NO_RESULTS_RETURNED("No results were returned. "),
	AUTHENTICATION_FAILED("Authentication failed. "),
	COULD_NOT_UPDATE_RECORD("Could not update record. "),
	COULD_NOT_DELETE_RECORD("Could not delete record. "),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified. "),
	BAD_REQUEST("Bad request. "),
	COULD_NOT_GENERATE_UNIQUE_STATE("Could not generate unique state. Try again. "),
	TOO_MANY_REDDIT_REQUESTS("Too many requests to the Reddit API have been made. Please try again after: ");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
