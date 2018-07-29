package com.github.kevinmussi.itunesrp.data;

import java.util.Objects;

public class Track {
	
	public static final Track NULL_TRACK = new Track();
	
	private final String name;
	private final String artist;
	private final String album;
	private final Application app;
	private final TrackState state;
	
	private Track() {
		this.name = null;
		this.artist = null;
		this.album = null;
		this.app = null;
		this.state = null;
	}
	
	public Track(String name, String artist, String album, Application app, TrackState state) {
		this.name = Objects.requireNonNull(name);
		this.artist = Objects.requireNonNull(artist);
		this.album = Objects.requireNonNull(album);
		this.app = Objects.requireNonNull(app);
		this.state = Objects.requireNonNull(state);
	}
	
	public String getName() {
		return name;
	}
	
	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}
	
	public Application getApplication() {
	    return app;
	}
	
	public TrackState getState() {
		return state;
	}
	
	public boolean isNull() {
		return this == NULL_TRACK;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		return name.equals(other.name) &&
				album.equals(other.album) &&
				artist.equals(other.artist) &&
				app == other.app &&
				state == other.state;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	
}
