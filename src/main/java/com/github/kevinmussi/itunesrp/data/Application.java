package com.github.kevinmussi.itunesrp.data;

public enum Application {
    ITUNES("iTunes", "itunes-logo"),
    SPOTIFY("Spotify", "spotify-logo");
    
    private final String description;
    private final String imageKey;
    
    private Application(String description, String imageKey) {
        this.description = description;
        this.imageKey = imageKey;
    }
    
    public static Application fromString(String string) {
        return Application.valueOf(string.toUpperCase());
    }
    
    public String getImageKey() {
    	return imageKey;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
