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

import ca.tunestumbler.api.shared.dto.UserDTO;

@Entity(name = "subreddit")
public class SubredditEntity implements Serializable {
	private static final long serialVersionUID = 3861524434851634472L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subredditDTO")
	private String subredditId;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
