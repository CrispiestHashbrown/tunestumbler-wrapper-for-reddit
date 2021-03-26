package ca.tunestumbler.api.ui.model.response.playlist;

import java.util.List;

public class PlaylistResponseModel {
	private List<PlaylistModel> playlistModels;

	public List<PlaylistModel> getPlaylists() {
		return playlistModels;
	}

	public void setPlaylists(List<PlaylistModel> playlistModels) {
		this.playlistModels = playlistModels;
	}

}
