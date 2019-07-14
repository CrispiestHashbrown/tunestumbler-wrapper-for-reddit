package ca.tunestumbler.api.ui.model.response;

public enum ErrorPrefixes {
	USER_CONTROLLER("USER CONTROLLER: "),
	USER_SERVICE("USER SERVICE: "),
	AUTH_CONTROLLER("AUTH CONTROLLER "),
	AUTH_SERVICE("AUTH SERVICE: "),
	SUBREDDIT_CONTROLLER("SUBREDDIT CONTROLLER: "),
	SUBREDDIT_SERVICE("SUBREDDIT SERVICE: "),
	MULTIREDDIT_CONTROLLER("MULTIREDDIT CONTROLLER: "),
	MULTIREDDIT_SERVICE("MULTIREDDIT SERVICE: "),
	AGGREGATE_CONTROLLER("AGGREGATE CONTROLLER: "),
	AGGREGATE_SERVICE("AGGREGATE SERVICE: "),
	FILTERS_CONTROLLER("FILTERS CONTROLLER: "),
	FILTERS_SERVICE("FILTERS SERVICE: "),
	RESULTS_CONTROLLER("RESULTS CONTROLLER: "),
	RESULTS_SERVICE("RESULTS SERVICE: ");

	private String errorPrefix;

	ErrorPrefixes(String errorPrefix) {
		this.setErrorPrefix(errorPrefix);
	}

	/**
	 * @return the errorPrefix
	 */
	public String getErrorPrefix() {
		return errorPrefix;
	}

	/**
	 * @param errorPrefix the errorPrefix to set
	 */
	public void setErrorPrefix(String errorPrefix) {
		this.errorPrefix = errorPrefix;
	}

}
