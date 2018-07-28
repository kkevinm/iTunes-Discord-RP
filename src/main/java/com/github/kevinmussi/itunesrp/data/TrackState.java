package com.github.kevinmussi.itunesrp.data;

public enum TrackState {
	PLAYING("Playing"),
	PAUSED("Paused");
	
	private String description;
	
	private TrackState(String description) {
		this.description = description;
	}
	
	public static TrackState fromString(String string) {
	    return TrackState.valueOf(string.toUpperCase());
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
