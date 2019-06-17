package ca.tunestumbler.api.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "aggregate")
public class AggregateEntity implements Serializable {
	private static final long serialVersionUID = 275525728395895620L;

	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;

	@Id
	@Column(nullable = false, unique = true)
	private String aggregateId;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	@ManyToOne
	@JoinColumn(name = "subreddit_subredditId")
	private SubredditEntity subredditEntity;

	@ManyToOne
	@JoinColumn(name = "multireddit_multiredditId")
	private MultiredditEntity multiredditEntity;

	@Column(nullable = false)
	private String lastModified;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "aggregateEntity")
	private List<ResultsEntity> results;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public SubredditEntity getSubredditEntity() {
		return subredditEntity;
	}

	public void setSubredditEntity(SubredditEntity subredditEntity) {
		this.subredditEntity = subredditEntity;
	}

	public MultiredditEntity getMultiredditEntity() {
		return multiredditEntity;
	}

	public void setMultiredditEntity(MultiredditEntity multiredditEntity) {
		this.multiredditEntity = multiredditEntity;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<ResultsEntity> getResults() {
		return results;
	}

	public void setResults(List<ResultsEntity> results) {
		this.results = results;
	}

}
