package ca.tunestumbler.api.ui.model.request.filter.delete;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class IdsToDeleteRequestModel {
	@NotNull
	@NotEmpty
	@Valid
	private List<String> idsToDelete;

	public List<String> getIdsToDelete() {
		return idsToDelete;
	}

	public void setIdsToDelete(List<String> idsToDelete) {
		this.idsToDelete = idsToDelete;
	}

}
