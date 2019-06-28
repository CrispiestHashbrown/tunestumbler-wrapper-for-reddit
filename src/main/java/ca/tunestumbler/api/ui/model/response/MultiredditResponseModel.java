package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditObjectResponseModel;

public class MultiredditResponseModel {
	private List<MultiredditObjectResponseModel> multireddits;

	public List<MultiredditObjectResponseModel> getMultireddits() {
		return multireddits;
	}

	public void setMultireddits(List<MultiredditObjectResponseModel> multireddits) {
		this.multireddits = multireddits;
	}

}
