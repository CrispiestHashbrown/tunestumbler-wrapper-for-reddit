package ca.tunestumbler.api.ui.model.response;

public class ErrorObject {
	private String status;
	private String title;
	private String message;
	private String timestamp;

	public ErrorObject() {
	}

	public ErrorObject(String status, String title, String message, String timestamp) {
		this.status = status;
		this.title = title;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
