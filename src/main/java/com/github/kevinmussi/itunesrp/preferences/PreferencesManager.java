package com.github.kevinmussi.itunesrp.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PreferencesManager {
	
	private static final Logger LOGGER;
	
	public static final String PREFERENCES_PATH;
	public static final String PREFERENCES_FILE;
	
	private static final File FILE;
	
	private static Preferences prefs;
	
	static {
		LOGGER = Logger.getLogger(PreferencesManager.class.getName());
		
		PREFERENCES_PATH = System.getProperty("user.home")
				.concat("/Library/Application Support/com.github.kevinmussi.iTunesDiscordRP/");
		PREFERENCES_FILE = "preferences.dat";
		
		FILE = new File(PREFERENCES_PATH, PREFERENCES_FILE);
		if(!FILE.exists()) {
			setPreferences(Preferences.getDefault());
		} else {
			prefs = readPreferencesFromFile();
		}
	}
	
	private PreferencesManager() {
		super();
	}
	
	public static Preferences getPreferences() {
		return prefs.getCopy();
	}
	
	public static void setPreferences(Preferences preferences) {
		if(preferences != null) {
			prefs = preferences.getCopy();
			writePreferencesToFile(preferences);
		}
	}
	
	private static Preferences readPreferencesFromFile() {
		try(ObjectInputStream inputStream =
				new ObjectInputStream(new FileInputStream(FILE))) {
			Preferences pr = (Preferences) inputStream.readObject();
			LOGGER.log(Level.INFO, "Preferences correctly read from file.");
			return pr;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Something went wrong while reading from the preferences file: ", e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Unexpected error: ", e);
		}
		
		// File doesn't exist or some error occurred
		Preferences pr = Preferences.getDefault();
		setPreferences(pr);
		return pr;
	}
	
	private static void writePreferencesToFile(Preferences preferences) {
		if(preferences == null)
			return;
		createFile(FILE);
		try(ObjectOutputStream outputStream =
				new ObjectOutputStream(new FileOutputStream(FILE))) {
			outputStream.writeObject(preferences);
			LOGGER.log(Level.INFO, "The preferences file has been updated.");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Something went wrong while writing to the preferences file: ", e);
		}
	}
	
	private static void createFile(File file) {
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "An error occurred while creating new file: ", e);
			}
		}
	}
	
}
