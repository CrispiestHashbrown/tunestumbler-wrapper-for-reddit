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

@Entity(name = "multireddit")
public class MultiredditEntity implements Serializable {
	private static final long serialVersionUID = 9178020527307904668L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "multiredditEntity")
	private String multiredditId;

	@Column(nullable = false, length = 50)
	private String multireddit;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserEntity userEntity;

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

}
