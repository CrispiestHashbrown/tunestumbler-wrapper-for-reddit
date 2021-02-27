package ca.tunestumbler.api.io.repositories.specification;

public class SearchCriteria {
	private Object value;
	private String field;
	private SearchOperation operation;

	public SearchCriteria() {
	}

	public SearchCriteria(Object value, String field, SearchOperation operation) {
		this.value = value;
		this.field = field;
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public SearchOperation getOperation() {
		return operation;
	}

	public void setOperation(SearchOperation operation) {
		this.operation = operation;
	}

}
