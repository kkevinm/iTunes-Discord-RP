package com.github.kevinmussi.itunesrp.data;

import java.util.Objects;

public class Track {
	
	public static final Track NULL_TRACK = new Track();
	
	private final String name;
	private final String artist;
	private final String album;
	private final Application app;
	private final TrackState state;
	private final double currentPosition;
	private final double duration;
	private final int durationMinutes;
	private final int durationSeconds;
	private final int index;
	private final int albumSize;
	
	private Track() {
		this.name = null;
		this.artist = null;
		this.album = null;
		this.app = null;
		this.state = null;
		this.currentPosition = 0;
		this.duration = 0;
		this.index = 0;
		this.albumSize = 0;
		this.durationMinutes = 0;
		this.durationSeconds = 0;
	}
	
	public Track(String name, String artist, String album, TrackState state, double currentPosition, double duration,
			int index, int albumSize, Application app) {
		this.name = Objects.requireNonNull(name);
		this.artist = Objects.requireNonNull(artist);
		this.album = Objects.requireNonNull(album);
		this.app = Objects.requireNonNull(app);
		this.state = Objects.requireNonNull(state);
		this.currentPosition = currentPosition;
		this.duration = duration;
		this.durationMinutes = (int) duration / 60;
		this.durationSeconds = (int) (duration - durationMinutes * 60);
		this.index = index;
		this.albumSize = albumSize;
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
	
	public double getCurrentPosition() {
		return currentPosition;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public int getDurationMinutes() {
		return durationMinutes;
	}
	
	public int getDurationSeconds() {
		return durationSeconds;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getAlbumSize() {
		return albumSize;
	}
	
	public boolean isNull() {
		return this == NULL_TRACK;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("Name: ").append(name).append("\nArtist: ").append(artist).append("\nAlbum: ")
				.append(album).append("\nDuration: ").append(durationMinutes)
				.append(String.format(":%02d", durationSeconds)).toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null || getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		return name.equals(other.name) && album.equals(other.album) && artist.equals(other.artist) && app == other.app
				&& state == other.state && currentPosition == other.currentPosition && duration == other.duration
				&& index == other.index && albumSize == other.albumSize;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result + albumSize;
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		long temp;
		temp = Double.doubleToLongBits(currentPosition);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + index;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	
}
