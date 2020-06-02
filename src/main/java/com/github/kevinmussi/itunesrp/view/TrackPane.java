package com.github.kevinmussi.itunesrp.view;

import javax.swing.JTextPane;

import com.github.kevinmussi.itunesrp.data.Track;

public class TrackPane extends JTextPane {
	
	private static final long serialVersionUID = -1373967048611694815L;
	
	private static final String NO_TRACK_TEXT = "<html><div style='font-family: sans-serif; font-size: 12px;'>"
			+ "Currently there's no song playing.</div></html>";
	private static final String TRACK_TEXT_FORMAT = "<html><div style='font-family: sans-serif; font-size: 12px;'>"
			+ "Currently <i>%s</i> on <b>%s</b>:<ul><li><b>Name</b>: %s</li>"
			+ "<li><b>Artist</b>: %s</li><li><b>Album</b>: %s</li><li><b>Duration</b>: %d:%02d</li>"
			+ "<li>Track %d of %d</li></ul></div></html>";
	
	public TrackPane() {
		this.setContentType("text/html");
		this.setEditable(false);
		this.setVisible(true);
	}
	
	public void setTrack(Track track) {
		if(track == null)
			return;
		if(track.isNull()) {
			setText(NO_TRACK_TEXT);
		} else {
			String text = String.format(TRACK_TEXT_FORMAT, track.getState().toString().toLowerCase(),
					track.getApplication(), track.getName(), track.getArtist(), track.getAlbum(),
					track.getDurationMinutes(), track.getDurationSeconds(), track.getIndex(), track.getAlbumSize());
			setText(text);
		}
	}
	
}
