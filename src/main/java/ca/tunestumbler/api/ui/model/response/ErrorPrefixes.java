package ca.tunestumbler.api.ui.model.response;

public enum ErrorPrefixes {
	USER_SERVICE("USER SERVICE: "),
	AUTH_SERVICE("AUTH SERVICE: "),
	SUBREDDIT_SERVICE("SUBREDDIT SERVICE: "),
	MULTIREDDIT_SERVICE("MULTIREDDIT SERVICE: "),
	AGGREGATE_SERVICE("AGGREGATE SERVICE: "),
	FILTERS_SERVICE("FILTERS SERVICE: "),
	RESULTS_SERVICE("RESULTS SERVICE: "),
	USER_ERROR("USER ERROR: ");

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
