package com.github.kevinmussi.itunesrp.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.github.kevinmussi.itunesrp.data.Track;

public class TrackTextArea extends JTextArea {
	
	private static final long serialVersionUID = -1373967048611694815L;
	
	private static final String NO_TRACK_TEXT = "Currently here's no song playing.";
	private static final String TRACK_TEXT_FORMAT = "Currently %s on %s:<br>"
			+ "Name: %s<br>Artist: %s<br>Album: %s <br>Duration: %d:%d";
	
	public TrackTextArea() {
		TitledBorder border = BorderFactory.createTitledBorder("Song playing");
		border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		border.setBorder(new LineBorder(new Color(184, 206, 227), 1));
		this.setBorder(border);
		this.setLineWrap(true);
		this.setEditable(false);
		this.setOpaque(false);
		this.setVisible(true);
	}
	
	public void setTrack(Track track) {
		if(track == null)
			return;
		if(track.isNull()) {
			setText(NO_TRACK_TEXT);
		} else {
			double duration = track.getDuration();
			int minutes = (int) duration/60;
			int seconds = (int) (duration-minutes*60);
			String text = String.format(TRACK_TEXT_FORMAT,
					track.getState(), track.getApplication(),
					track.getName(), track.getArtist(),
					track.getAlbum(), minutes, seconds);
			setText(text);
		}
	}
	
}
