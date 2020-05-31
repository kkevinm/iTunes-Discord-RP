package com.github.kevinmussi.itunesrp.preferences;

import java.io.Serializable;

public class Preferences implements Serializable {
	
	private static final long serialVersionUID = 8564842536502299868L;
	
	private boolean autoConnect;
	private int imageId;
	private boolean useEmojis;
	
	Preferences() {
		super();
	}
	
	Preferences(boolean autoConnect, int imageId, boolean useEmojis) {
		this.autoConnect = autoConnect;
		this.imageId = imageId;
		this.useEmojis = useEmojis;
	}
	
	public static Preferences getDefault() {
		return new Preferences(false, 1, true);
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
	
	public Preferences getCopy() {
		return new Preferences(autoConnect, imageId, useEmojis);
	}
	
}
