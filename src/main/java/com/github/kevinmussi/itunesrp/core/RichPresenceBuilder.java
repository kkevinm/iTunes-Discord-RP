package com.github.kevinmussi.itunesrp.core;

import java.time.OffsetDateTime;

import com.github.kevinmussi.itunesrp.data.FieldPosition;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.preferences.Preferences;
import com.github.kevinmussi.itunesrp.preferences.PreferencesManager;
import com.jagrosh.discordipc.entities.RichPresence;

public class RichPresenceBuilder {
	
	private static final String EMOJI_SONG;
	private static final String EMOJI_ARTIST;
	private static final String EMOJI_ALBUM;
	
	static {
		int songEmojiCodePoint = 127926;
		int artistEmojiCodePoint = 128100;
		int albumEmojiCodePoint = 128191;
		
		EMOJI_SONG = new String(Character.toChars(songEmojiCodePoint));
		EMOJI_ARTIST = new String(Character.toChars(artistEmojiCodePoint));
		EMOJI_ALBUM = new String(Character.toChars(albumEmojiCodePoint));
	}
	
	private final Track track;
	private final Preferences preferences;
	
	private String field1 = " ";
	private String field2 = " ";
	private String field3 = " ";
	private String field4 = " ";
	
	public RichPresenceBuilder(Track track) {
		this.track = track;
		this.preferences = PreferencesManager.getPreferences();
	}
	
	public RichPresence build() {
		RichPresence.Builder builder = new RichPresence.Builder();
		
		initFields();
		buildFields();
		
		String rpDetails = field1 + field2;
		String rpState = field3 + field4;
		String state = track.getState().toString();
		
		if(track.getState() == TrackState.PLAYING) {
			OffsetDateTime start = OffsetDateTime.now().minusSeconds((long) track.getCurrentPosition());
			builder.setStartTimestamp(start);
			builder.setInstance(true);
		}
		
		builder.setDetails(rpDetails);
		builder.setState(rpState);
		builder.setSmallImage(state.toLowerCase(), state);
		builder.setLargeImage(track.getApplication().getImageKey(), track.getApplication().toString());
		int index = track.getIndex();
		// If the album isn't set to be displayed, don't show the index and size too.
		int size = preferences.getAlbumPosition() == FieldPosition.NONE ? 0 : track.getAlbumSize();
		if(index > 0 && size > 0 && index <= size) {
			builder.setParty("aa", index, size);
		}
		
		return builder.build();
	}
	
	private void initFields() { // NOSONAR
		FieldPosition artistPosition = preferences.getArtistPosition();
		FieldPosition albumPosition = preferences.getAlbumPosition();
		String artist = track.getArtist();
		String album = track.getAlbum();
		boolean useEmoji = preferences.getUseEmojis();
		
		field1 = useEmoji ? EMOJI_SONG + " " + track.getName() : track.getName();
		
		if(artistPosition == FieldPosition.TOP) {
			field2 = useEmoji ? " " + EMOJI_ARTIST + " " + artist + " " : ", by " + artist;
			if(albumPosition == FieldPosition.BOTTOM) {
				field3 = useEmoji ? EMOJI_ALBUM + " " + album : "From " + album;
			}
		} else if(albumPosition == FieldPosition.TOP) {
			field2 = useEmoji ? " " + EMOJI_ALBUM + " " + album : ", from " + album;
			if(artistPosition == FieldPosition.BOTTOM) {
				field3 = useEmoji ? EMOJI_ARTIST + " " + artist : "By " + artist;
			}
		} else {
			if(artistPosition == FieldPosition.BOTTOM) {
				field3 = useEmoji ? EMOJI_ARTIST + " " + artist : "By " + artist;
				if(albumPosition == FieldPosition.BOTTOM) {
					field4 = useEmoji ? " " + EMOJI_ALBUM + " " + album : ", from " + album;
				}
			} else if(albumPosition == FieldPosition.BOTTOM) {
				field3 = useEmoji ? EMOJI_ALBUM + " " + album : "From " + album;
			}
		}
	}
	
	// Fix the fields' length to make everything stay in one line
	private void buildFields() {
		int max = 48;
		if(field2.length() != 0) {
			// Trim field1 and field2
			if(field1.length() > 27) {
				field1 = field1.substring(0, 26) + "...";
				max = 52;
			}
			if(field1.length() + field2.length() > max) {
				field2 = field2.substring(0, max - field1.length()) + "...";
			}
		} else if(field3.length() != 0 && field4.length() != 0) {
			// Trim field3 and field4
			if(field3.length() > 27) {
				field3 = field3.substring(0, 26) + "...";
				max = 52;
			}
			if(field3.length() + field4.length() > max) {
				field4 = field4.substring(0, max - field4.length()) + "...";
			}
		}
	}
	
}
