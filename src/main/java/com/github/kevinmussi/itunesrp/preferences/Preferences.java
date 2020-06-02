package com.github.kevinmussi.itunesrp.preferences;

import java.io.Serializable;

import com.github.kevinmussi.itunesrp.data.FieldPosition;

public class Preferences implements Serializable {
	
	private static final long serialVersionUID = -1351752435859677313L;
	
	private boolean autoConnect;
	private int imageId;
	private boolean useEmojis;
	private FieldPosition artistPosition;
	private FieldPosition albumPosition;
	
	Preferences() {
		super();
	}
	
	Preferences(boolean autoConnect, int imageId, boolean useEmojis, FieldPosition artistPosition,
			FieldPosition albumPosition) {
		this.autoConnect = autoConnect;
		this.imageId = imageId;
		this.useEmojis = useEmojis;
		this.artistPosition = artistPosition;
		this.albumPosition = albumPosition;
	}
	
	public static Preferences getDefault() {
		return new Preferences(false, 1, true, FieldPosition.BOTTOM, FieldPosition.BOTTOM);
	}
	
	public boolean getAutoConnect() {
		return autoConnect;
	}
	
	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
	
	public int getImageId() {
		return imageId;
	}
	
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
	public boolean getUseEmojis() {
		return useEmojis;
	}
	
	public void setUseEmojis(boolean useEmojis) {
		this.useEmojis = useEmojis;
	}
	
	public FieldPosition getArtistPosition() {
		return artistPosition;
	}
	
	public void setArtistPosition(FieldPosition artistPosition) {
		this.artistPosition = artistPosition;
	}
	
	public FieldPosition getAlbumPosition() {
		return albumPosition;
	}
	
	public void setAlbumPosition(FieldPosition albumPosition) {
		this.albumPosition = albumPosition;
	}
	
	public Preferences getCopy() {
		return new Preferences(autoConnect, imageId, useEmojis, artistPosition, albumPosition);
	}
	
}
