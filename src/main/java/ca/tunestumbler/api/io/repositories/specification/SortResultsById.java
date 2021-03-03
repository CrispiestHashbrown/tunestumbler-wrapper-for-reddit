package ca.tunestumbler.api.io.repositories.specification;

import java.util.Comparator;

import ca.tunestumbler.api.io.entity.ResultsEntity;

public class SortResultsById implements Comparator<ResultsEntity> {

	@Override
	public int compare(ResultsEntity a, ResultsEntity b) {
		return Long.compare(a.getId(), b.getId());
	}

}
