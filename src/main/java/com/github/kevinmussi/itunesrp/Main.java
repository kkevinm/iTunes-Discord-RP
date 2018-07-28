package com.github.kevinmussi.itunesrp;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main {
	
	private static final Logger logger;
	private static final String scriptFileName;
	
	static {
		logger = Logger.getLogger("iTunesDiscordRP." + Main.class.getSimpleName() + " logger");
		scriptFileName = "/itunes_track_info_script.txt";
	}
	
	private Main() {
		super();
	}
	
	private static String getOsVersion() {
		return System.getProperty("os.name");
	}
	
	private static String getScript() {
		Scanner scanner = new Scanner(Main.class.getResourceAsStream(scriptFileName));
		scanner.useDelimiter("\\A");
		String contents = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		return contents;
	}
	
	public static void main(String[] args) {
		if(!getOsVersion().startsWith("Mac OS X")) {
			logger.log(Level.SEVERE, "This application works only on MacOS!");
			return;
		}
		
		String script = getScript();
		
	}
	
}
