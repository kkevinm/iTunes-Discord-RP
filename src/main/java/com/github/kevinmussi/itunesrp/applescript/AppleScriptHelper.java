package com.github.kevinmussi.itunesrp.applescript;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppleScriptHelper {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger("iTunesDiscordRP." + AppleScriptHelper.class.getSimpleName() + " logger");
	}
	
	private Runtime runtime;
	private String[] args = {"osascript", "-e", null};
	
	public AppleScriptHelper() {
		this.runtime = Runtime.getRuntime();
	}
	
	public String execute(String script) {
		String result = "";
		args[2] = Objects.requireNonNull(script);
		
		try {
			ProcessBuilder builder = new ProcessBuilder("osascript", "-e", script);
			Process process = builder.start();
			Scanner scanner = new Scanner(process.getInputStream());
			scanner.useDelimiter("\\A");
			if(scanner.hasNext()) {
				result = scanner.next();
			}
			scanner.close();
			return result;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "The script:\n " + script + "\ndid not execute correctly", e);
		}
		return result;
	}
	
}
