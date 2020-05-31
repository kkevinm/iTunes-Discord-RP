package com.github.kevinmussi.itunesrp.preferences;

import java.io.Serializable;

public class Preferences implements Serializable {
	
	private static final long serialVersionUID = 8564842536502299868L;
	
	private boolean autoConnect;
	private int imageId;
	
	Preferences() {
		super();
	}
	
	Preferences(boolean autoConnect, int imageId) {
		this.autoConnect = autoConnect;
		this.imageId = imageId;
	}
	
	public static Preferences getDefault() {
		return new Preferences(false, 1);
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
	
	public Preferences getCopy() {
		return new Preferences(autoConnect, imageId);
	}
	
}
