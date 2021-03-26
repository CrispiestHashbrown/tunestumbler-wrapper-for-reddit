package ca.tunestumbler.api.io.repositories.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ca.tunestumbler.api.io.entity.ResultsEntity;

public class ResultsSpecification implements Specification<ResultsEntity> {
	private static final long serialVersionUID = -244093893557352665L;

	private transient List<SearchCriteria> list;
	private transient List<SearchCriteria> likeSelectedDomains;
	private transient List<SearchCriteria> likeSelectedKeywords;

	public ResultsSpecification() {
		this.list = new ArrayList<>();
		this.likeSelectedDomains = new ArrayList<>();
		this.likeSelectedKeywords = new ArrayList<>();
	}

	public void add(SearchCriteria criteria) {
		if (!(criteria.getValue() == null || criteria.getValue().toString().isEmpty())) {
			this.list.add(criteria);
		}
	}

	public void addLikeSelectedDomains(SearchCriteria criteria) {
		if (!(criteria.getValue() == null || criteria.getValue().toString().isEmpty())) {
			this.likeSelectedDomains.add(criteria);
		}
	}

	public void addLikeSelectedKeywords(SearchCriteria criteria) {
		if (!(criteria.getValue() == null || criteria.getValue().toString().isEmpty())) {
			this.likeSelectedKeywords.add(criteria);
		}
	}

	@Override
	public Predicate toPredicate(Root<ResultsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		List<Predicate> selectedDomainPredicates = new ArrayList<>();
		List<Predicate> selectedKeywordPredicates = new ArrayList<>();
		for (SearchCriteria criteria : list) {
			if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
				if (isString(criteria.getValue())) {
					predicates.add(criteriaBuilder.equal(root.get(criteria.getField()),
							criteria.getValue().toString().toLowerCase()));
				} else {
					predicates.add(criteriaBuilder.equal(root.get(criteria.getField()), criteria.getValue()));
				}
			} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_OR_EQUAL)) {
				predicates.add(criteriaBuilder.ge(root.get(criteria.getField()), (Number) criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.NOT_LIKE)) {
				predicates.add(criteriaBuilder.notLike(root.get(criteria.getField()),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
			}
		}

		if (!likeSelectedDomains.isEmpty()) {
			for (SearchCriteria criteria : likeSelectedDomains) {
				selectedDomainPredicates.add(criteriaBuilder.like(root.get(criteria.getField()),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
			}
		} else {
			selectedDomainPredicates.add(criteriaBuilder.like(root.get("domain"), "%%"));
		}

		if (!likeSelectedKeywords.isEmpty()) {
			for (SearchCriteria criteria : likeSelectedKeywords) {
				selectedKeywordPredicates.add(criteriaBuilder.like(root.get(criteria.getField()),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
			}
		} else {
			selectedKeywordPredicates.add(criteriaBuilder.like(root.get("title"), "%%"));
		}

		return criteriaBuilder.and(criteriaBuilder.and(predicates.toArray(new Predicate[0])),
				criteriaBuilder.or(selectedDomainPredicates.toArray(new Predicate[0])),
				criteriaBuilder.or(selectedKeywordPredicates.toArray(new Predicate[0])));
	}

	private boolean isString(Object any) {
		return any instanceof String;
	}

}
