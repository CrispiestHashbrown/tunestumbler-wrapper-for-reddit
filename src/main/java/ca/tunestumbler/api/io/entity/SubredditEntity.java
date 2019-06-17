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

@Entity(name = "subreddit")
public class SubredditEntity implements Serializable {
	private static final long serialVersionUID = 3861524434851634472L;

	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;

	@Id
	@Column(nullable = false, unique = true)
	private String subredditId;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	@Column(nullable = false)
	private String lastModified;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subredditEntity")
	private List<AggregateEntity> aggregate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubredditId() {
		return subredditId;
	}

	public void setSubredditId(String subredditId) {
		this.subredditId = subredditId;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<AggregateEntity> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateEntity> aggregate) {
		this.aggregate = aggregate;
	}

}
