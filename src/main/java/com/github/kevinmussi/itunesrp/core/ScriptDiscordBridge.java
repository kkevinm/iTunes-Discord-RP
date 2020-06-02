package com.github.kevinmussi.itunesrp.core;

import com.github.kevinmussi.itunesrp.data.Application;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.observer.Observable;
import com.github.kevinmussi.itunesrp.observer.Observer;

public class ScriptDiscordBridge extends Observable<Track> implements Observer<String> {
	
	public ScriptDiscordBridge() {
		super();
	}
	
	@Override
	public void onUpdate(String message) {
		if(message == null || message.length() == 0) {
			return;
		}
		Track track;
		if(message.equals("STOPPED")) {
			// There's no playing/paused song
			track = Track.NULL_TRACK;
		} else {
			track = getTrackfromRecord(message, ScriptHelper.TRACK_RECORD_SEPARATOR);
		}
		sendUpdate(track);
	}
	
	/**
	 * <p>
	 * This method attempts to create a new {@link Track} object based on the
	 * {@code record} and {@code separator} provided.
	 * <p>
	 * This method expects a record that contains 4 field separated by the separator
	 * provided, where the fields are:
	 * <li>track name (it <b>cannot</b> be empty).</li>
	 * <li>track artist (it can be empty).</li>
	 * <li>track album (it can be empty).</li>
	 * <li>track state: a string that can be either "PLAYING" or "PAUSED".</li>
	 * <li>current position: the time elapsed in seconds from when the track
	 * started.</li>
	 * <li>track duration: the total duration of the track.</li>
	 * <li>track index: the index of the track in its album.</li>
	 * <li>album size: the amount of tracks in the current album.</li>
	 * <p>
	 * 
	 * @param previousTrack
	 * @param record
	 * @param separator
	 * @return {@code null} if the record or the separator have length equal to 0 or
	 *         if the result of splitting the record based on the separator has an
	 *         incorrect format. Otherwise, the {@code Track} with the fields
	 *         contained in the record.
	 */
	private Track getTrackfromRecord(String record, String separator) {
		if(record.length() == 0 || separator.length() == 0) {
			return null;
		}
		String[] fields = record.split(separator);
		if(fields == null || fields.length != 8) {
			return null;
		}
		
		String name = fields[0];
		if(name.length() == 0) {
			return null;
		}
		String artist = fields[1].length() == 0 ? "Unknown Artist" : fields[1];
		String album = fields[2].length() == 0 ? "Unknown Album" : fields[2];
		
		TrackState state = TrackState.fromString(fields[3]);
		double currentPosition = Double.parseDouble(fields[4].replace(',', '.'));
		double duration = Double.parseDouble(fields[5].replace(',', '.'));
		int index = Integer.parseInt(fields[6]);
		int albumSize = Integer.parseInt(fields[7].trim());
		
		return new Track(name, artist, album, state, currentPosition, duration, index, albumSize, Application.ITUNES);
	}
	
}
