package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.aggregate.AggregateObjectResponseModel;

public class AggregateResponseModel {
	private List<AggregateObjectResponseModel> aggregate;

	public List<AggregateObjectResponseModel> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateObjectResponseModel> aggregate) {
		this.aggregate = aggregate;
	}

}
