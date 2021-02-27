package ca.tunestumbler.api.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity(name = "filters")
public class FiltersEntity implements Serializable {
	private static final long serialVersionUID = 661201748897792032L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(name = "filters_id", nullable = false, unique = true)
	private String filtersId;

	@Column()
	private String userId;

	@Column(nullable = false)
	@Size(min = 1, max = 21)
	private String subreddit;

	@Column()
	private Integer minScore = 1;

	@Column(nullable = false)
	private Boolean allowNSFWFlag = false;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_filters_id", referencedColumnName = "filters_id")
	private List<ExcludedDomainEntity> excludedDomains;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_filters_id", referencedColumnName = "filters_id")
	private List<ExcludedKeywordEntity> excludedKeywords;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_filters_id", referencedColumnName = "filters_id")
	private List<SelectedDomainEntity> selectedDomains;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_filters_id", referencedColumnName = "filters_id")
	private List<SelectedKeywordEntity> selectedKeywords;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	public Integer getMinScore() {
		return minScore;
	}

	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}

	public Boolean getAllowNSFWFlag() {
		return allowNSFWFlag;
	}

	public void setAllowNSFWFlag(Boolean allowNSFWFlag) {
		this.allowNSFWFlag = allowNSFWFlag;
	}

	public List<ExcludedDomainEntity> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<ExcludedDomainEntity> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public List<ExcludedKeywordEntity> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<ExcludedKeywordEntity> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	public List<SelectedDomainEntity> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<SelectedDomainEntity> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public List<SelectedKeywordEntity> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<SelectedKeywordEntity> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}
