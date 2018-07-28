package com.github.kevinmussi.itunesrp.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.github.kevinmussi.itunesrp.data.Application;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackMessage;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.observer.Observable;
import com.github.kevinmussi.itunesrp.observer.Observer;

public class AppleScriptDiscordBridge 
		extends Observable<TrackMessage> implements Observer<String> {
	
	private final Lock lock = new ReentrantLock();
	
	private volatile Track previousTrack;
	private volatile Track currentTrack;
	
	public AppleScriptDiscordBridge() {
		this.previousTrack = null;
		this.currentTrack = null;
	}
	
	@Override
	public void update(String message) {
		if(message == null || message.length() == 0) {
			return;
		}
		TrackMessage trackMessage;
		if(message.equals("STOPPED")) {
			// There's no song playing or paused
			trackMessage = TrackMessage.NO_TRACK_MESSAGE;
		} else {
			lock.lock();
			try {
				currentTrack = fromRecord(previousTrack, message, AppleScriptHelper.TRACK_RECORD_SEPARATOR);
				boolean isNewTrack = previousTrack != currentTrack;
				trackMessage = new TrackMessage(currentTrack, isNewTrack);
				
				// Set previousTrack = currentTrack for the next update
				previousTrack = currentTrack;
			} finally {
				lock.unlock();
			}
		}
		notifyObservers(trackMessage);
	}
	
	/**
	 * <p>This method attempts to create a new {@link Track} object based on the
	 * {@code record} and {@code separator} provided, given the previous {@code Track}
	 * that was created.
	 * <p>This method expects a record that contains 6 field separated by the separator
	 * provided, where the fields are:
	 * <li>"new track" flag: a flag that can be either "Y" or "N" depending on if the
	 * record refers to a new track or if the previous one can be used.</li>
	 * <li>track name (it <b>cannot</b> be empty).</li>
	 * <li>track artist (it can be empty).</li>
	 * <li>track album (it can be empty).</li>
	 * <li>track state: a string that can be either "PLAYING" or "PAUSED".</li>
	 * <li>"artwork missing" flag: a flag that can be either "Y" or "N" depending on if the
	 * track has an artwork.</li>
	 * <p>
	 * If the "new track" flag is "N", the method will set {@code previousTrack.state} with
	 * the one contained in the record, and then return {@code previousTrack}.
	 * Otherwise, a new track will be created with the fields contained in the record and then returned.
	 * 
	 * @param previousTrack
	 * @param record
	 * @param separator
	 * @return {@code null} if the record or the separator have length equal to 0
	 *         or if the result of splitting the record based on the separator has
	 *         an incorrect format.
	 *         Otherwise, the {@code Track} with the fields contained in the record.
	 */
	private static Track fromRecord(Track previousTrack, String record, String separator) {
		if(record.length() == 0 || separator.length() == 0) {
			return null;
		}
		String[] fields = record.split(separator);
		if(fields == null || fields.length != 6) {
			return null;
		}
		
		if(fields[1].length() == 0) {
			// The track name can't be empty
			return null;
		}
		
		TrackState state = TrackState.fromString(fields[4]);
		if(fields[0].equals("N")) {
			previousTrack.setState(state);
			return previousTrack;
		}
		
		boolean isArtworkAvailable;
		if(fields[5].equals("Y"))
			isArtworkAvailable = true;
		else if(fields[5].equals("N"))
			isArtworkAvailable = false;
		else
			return null;
		
		return new Track(fields[1], fields[2], fields[3],
		        isArtworkAvailable, Application.ITUNES, state);
	}

}
