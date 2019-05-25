package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.tunestumbler.api.shared.dto.UserDTO;

@Entity(name = "multireddit")
public class MultiredditEntity implements Serializable {
	private static final long serialVersionUID = 9178020527307904668L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String multiredditId;

	@Column(nullable = false, length = 50)
	private String multireddit;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserDTO userDTO;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
