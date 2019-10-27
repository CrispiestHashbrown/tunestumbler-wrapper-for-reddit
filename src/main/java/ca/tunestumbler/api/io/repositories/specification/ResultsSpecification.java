package ca.tunestumbler.api.io.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import ca.tunestumbler.api.io.entity.ResultsEntity;

public class ResultsSpecification {

	public static Specification<ResultsEntity> withUserId(String userId) {
		if (userId == null || userId.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
		}
	}

	public static Specification<ResultsEntity> withStartId(Long startId) {
		if (startId == null) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("startId"), startId);
		}
	}

	public static Specification<ResultsEntity> withSubreddit(String subreddit) {
		if (subreddit == null || subreddit.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("subreddit"), subreddit);
		}
	}

	public static Specification<ResultsEntity> withMinScore(Integer score) {
		if (score == null) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("score"), score);
		}
	}

	public static Specification<ResultsEntity> withNSFW(Boolean isNsfw) {
		if (isNsfw == null) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isNsfw"), isNsfw);
		}
	}

	public static Specification<ResultsEntity> withDomainOnly(String domain) {
		if (domain == null || domain.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("domain"), "%" + domain + "%");
		}
	}

	public static Specification<ResultsEntity> withoutDomain(String domain) {
		if (domain == null || domain.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.notLike(root.get("domain"), "%" + domain + "%");
		}
	}

	public static Specification<ResultsEntity> withTitleKeyword(String keyword) {
		if (keyword == null || keyword.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
		}
	}

	public static Specification<ResultsEntity> withoutTitleKeyword(String keyword) {
		if (keyword == null || keyword.isEmpty()) {
			return null;
		} else {
			return (root, query, criteriaBuilder) -> criteriaBuilder.notLike(root.get("title"), "%" + keyword +  "%");
		}
	}

}
