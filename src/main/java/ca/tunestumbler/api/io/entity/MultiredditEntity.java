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

@Entity(name = "multireddit")
public class MultiredditEntity implements Serializable {
	private static final long serialVersionUID = 9178020527307904668L;

	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;

	@Id
	@Column(nullable = false, unique = true)
	private String multiredditId;

	@Column(nullable = false, length = 50)
	private String multireddit;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	@Column()
	private String userId;

	@Column(nullable = false)
	private String lastModified;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "multiredditEntity")
	private List<AggregateEntity> aggregate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMultiredditId() {
		return multiredditId;
	}

	public void setMultiredditId(String multiredditId) {
		this.multiredditId = multiredditId;
	}

	public String getMultireddit() {
		return multireddit;
	}

	public void setMultireddit(String multireddit) {
		this.multireddit = multireddit;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
