package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "subreddit_aggregate")
public class SubredditAggregateEntity implements Serializable {
	private static final long serialVersionUID = 275525728395895620L;

	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;

	@Id
	@Column(nullable = false, unique = true)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subredditAggregateEntity")
	private String subredditAggregateId;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserEntity userEntity;

	@ManyToOne
	@JoinColumn(name = "subreddit_id")
	private SubredditEntity subredditEntity;

	@ManyToOne
	@JoinColumn(name = "multireddit_id")
	private MultiredditEntity multiredditEntity;

	@Column(nullable = false)
	private String datetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubredditAggregateId() {
		return subredditAggregateId;
	}

	public void setSubredditAggregateId(String subredditAggregateId) {
		this.subredditAggregateId = subredditAggregateId;
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

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
