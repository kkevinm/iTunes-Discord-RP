package com.github.kevinmussi.itunesrp.data;

import java.util.Objects;

public class TrackMessage {
    
	public static final TrackMessage NO_TRACK_MESSAGE;
	
	static {
		NO_TRACK_MESSAGE = new TrackMessage();
	}
	
    private final Track track;
    private final boolean isNewTrack;
    
    private TrackMessage() {
    	this.track = null;
    	this.isNewTrack = false;
    }
    
    public TrackMessage(Track track, boolean isNewTrack) {
        this.track = Objects.requireNonNull(track);
        this.isNewTrack = isNewTrack;
    }

	public Track getTrack() {
		return track;
	}

	public boolean isNewTrack() {
		return isNewTrack;
	}
    
}
