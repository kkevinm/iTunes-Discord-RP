package com.github.kevinmussi.itunesrp.core;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kevinmussi.itunesrp.observer.Observable;

public class AppleScriptHelper extends Observable<String> {
	
	public static final String TRACK_RECORD_SEPARATOR = ";;";
	
	private final Logger logger;
	
	public AppleScriptHelper() {
		logger = Logger.getLogger("iTunesDiscordRP." + this.getClass().getSimpleName() + " logger");
	}
	
	public void execute(String script) {
		try {
			ProcessBuilder builder = new ProcessBuilder("osascript", "-e", script);
			// The script logs its messages to stderr, so we need to redirect it to the stdout
			builder.redirectErrorStream(true);
			Process process = builder.start();
			Scanner scanner = new Scanner(process.getInputStream());
			scanner.useDelimiter("\n");
			while(process.isAlive()) {
				if(scanner.hasNext()) {
					notifyObservers(scanner.next());
				}
			}
			scanner.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "The script:\n " + script + "\ndid not execute correctly", e);
		}
	}
	
}
