package com.github.kevinmussi.itunesrp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;

import com.github.kevinmussi.itunesrp.applescript.AppleScriptHelper;
import com.github.kevinmussi.itunesrp.applescript.AppleScripts;

public final class Main {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger("iTunesDiscordRP." + Main.class.getSimpleName() + " logger");
	}
	
	private Main() {
		super();
	}
	
	private static String getOsVersion() {
		return System.getProperty("os.name");
	}
	
	public static void main(String[] args) throws ScriptException {
		if(!getOsVersion().startsWith("Mac OS X")) {
			logger.log(Level.SEVERE, "This application works only on MacOS!");
			return;
		}
		
		AppleScriptHelper helper = new AppleScriptHelper();
		System.out.println(helper.execute(AppleScripts.ITUNES_TRACK_INFO_SCRIPT));
	}
	
}
