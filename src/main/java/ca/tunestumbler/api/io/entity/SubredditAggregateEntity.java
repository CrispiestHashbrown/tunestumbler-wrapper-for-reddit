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

import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

@Entity(name = "subreddit_aggregate")
public class SubredditAggregateEntity implements Serializable {
	private static final long serialVersionUID = 275525728395895620L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "subredditAggregateDTO")
	private String subredditAggregateId;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserDTO userDTO;

	@ManyToOne
	@JoinColumn(name = "subreddit_id")
	private SubredditDTO subredditDTO;

	@ManyToOne
	@JoinColumn(name = "multireddit_id")
	private MultiredditDTO multiredditDTO;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public SubredditDTO getSubredditDTO() {
		return subredditDTO;
	}

	public void setSubredditDTO(SubredditDTO subredditDTO) {
		this.subredditDTO = subredditDTO;
	}

	public MultiredditDTO getMultiredditDTO() {
		return multiredditDTO;
	}

	public void setMultiredditDTO(MultiredditDTO multiredditDTO) {
		this.multiredditDTO = multiredditDTO;
	}

}
