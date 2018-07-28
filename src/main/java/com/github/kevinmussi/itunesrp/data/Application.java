package com.github.kevinmussi.itunesrp.data;

public enum Application {
    ITUNES("iTunes"),
    SPOTIFY("Spotify");
    
    private String description;
    
    private Application(String description) {
        this.description = description;
    }
    
    public static Application fromString(String string) {
        return Application.valueOf(string.toUpperCase());
    }
    
    @Override
    public String toString() {
        return description;
    }
}
