package com.github.kevinmussi.itunesrp.data;

import com.github.kevinmussi.itunesrp.preferences.PreferencesManager;

public enum Application {
	ITUNES("iTunes", "itunes-logo"),
	SPOTIFY("Spotify", "spotify-logo");
	
	private final String description;
	private final String imageKeyBase;
	
	private Application(String description, String imageKeyBase) {
		this.description = description;
		this.imageKeyBase = imageKeyBase;
	}
	
	public static Application fromString(String string) {
		return Application.valueOf(string.toUpperCase());
	}
	
	public String getImageKey() {
		int imageId = PreferencesManager.getPreferences().getImageId();
		if(imageId == 0) {
			return null;
		} else {
			return imageKeyBase + "-" + imageId;
		}
	}
	
	@Override
	public String toString() {
		return description;
	}
}
