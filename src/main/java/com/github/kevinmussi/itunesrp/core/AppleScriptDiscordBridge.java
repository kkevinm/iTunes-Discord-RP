package com.github.kevinmussi.itunesrp.core;

import com.github.kevinmussi.itunesrp.data.Application;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.observer.Observable;
import com.github.kevinmussi.itunesrp.observer.Observer;

public class AppleScriptDiscordBridge 
		extends Observable<Track> implements Observer<String> {
	
	public AppleScriptDiscordBridge() {
		super();
	}
	
	@Override
	public void update(String message) {
		if(message == null || message.length() == 0) {
			return;
		}
		Track track;
		if(message.equals("STOPPED")) {
			// There's no song playing or paused
			track = Track.NULL_TRACK;
		} else {
			track = getTrackfromRecord(message, AppleScriptHelper.TRACK_RECORD_SEPARATOR);
			
		}
		notifyObservers(track);
	}
	
	/**
	 * <p>This method attempts to create a new {@link Track} object based on the
	 * {@code record} and {@code separator} provided.
	 * <p>This method expects a record that contains 4 field separated by the separator
	 * provided, where the fields are:
	 * <li>track name (it <b>cannot</b> be empty).</li>
	 * <li>track artist (it can be empty).</li>
	 * <li>track album (it can be empty).</li>
	 * <li>track state: a string that can be either "PLAYING" or "PAUSED".</li>
	 * <p>
	 * 
	 * @param previousTrack
	 * @param record
	 * @param separator
	 * @return {@code null} if the record or the separator have length equal to 0
	 *         or if the result of splitting the record based on the separator has
	 *         an incorrect format.
	 *         Otherwise, the {@code Track} with the fields contained in the record.
	 */
	private Track getTrackfromRecord(String record, String separator) {
		if(record.length() == 0 || separator.length() == 0) {
			return null;
		}
		String[] fields = record.split(separator);
		if(fields == null || fields.length != 4) {
			return null;
		}
		
		if(fields[0].length() == 0) {
			// The track name can't be empty
			return null;
		}
		
		TrackState state = TrackState.fromString(fields[3]);
		
		return new Track(fields[0], fields[1], fields[2], Application.ITUNES, state);
	}

}
