package com.github.kevinmussi.itunesrp.data;

public enum TrackState {
	PLAYING("Playing"),
	PAUSED("Paused");
	
	private String description;
	
	private TrackState(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
