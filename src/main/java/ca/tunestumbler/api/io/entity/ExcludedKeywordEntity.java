package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "excluded_keywords")
public class ExcludedKeywordEntity implements Serializable {
	private static final long serialVersionUID = 6485909541902801908L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String excludedKeywordId;

	@Column(name = "parent_filters_id")
	private String filtersId;

	@Column()
	private String userId;

	@Column(length = 50)
	private String excludedKeyword;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExcludedKeywordId() {
		return excludedKeywordId;
	}

	public void setExcludedKeywordId(String excludedKeywordId) {
		this.excludedKeywordId = excludedKeywordId;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExcludedKeyword() {
		return excludedKeyword;
	}

	public void setExcludedKeyword(String excludedKeyword) {
		this.excludedKeyword = excludedKeyword;
	}

}
