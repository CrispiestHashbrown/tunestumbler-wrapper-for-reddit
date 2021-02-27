package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "selected_keywords")
public class SelectedKeywordEntity implements Serializable {
	private static final long serialVersionUID = -5066417758141791174L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String selectedKeywordId;

	@Column(name = "parent_filters_id")
	private String filtersId;

	@Column()
	private String userId;

	@Column(length = 50)
	private String selectedKeyword;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSelectedKeywordId() {
		return selectedKeywordId;
	}

	public void setSelectedKeywordId(String selectedKeywordId) {
		this.selectedKeywordId = selectedKeywordId;
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

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

}
