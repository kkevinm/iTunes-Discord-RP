package com.github.kevinmussi.itunesrp;

import java.awt.EventQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kevinmussi.itunesrp.core.AppleScriptDiscordBridge;
import com.github.kevinmussi.itunesrp.core.AppleScriptHelper;
import com.github.kevinmussi.itunesrp.core.DiscordHelper;
import com.github.kevinmussi.itunesrp.gui.MainFrame;
import com.github.kevinmussi.itunesrp.gui.View;

public final class Main {
	
	private static final Logger LOGGER;
	private static final String SCRIPT;
	
	static {
		LOGGER = Logger.getLogger(Main.class.getName() + "Logger");
		SCRIPT = "/itunes_track_info_script.applescript";
	}
	
	private Main() {
		super();
	}
	
	private static String getOsVersion() {
		return System.getProperty("os.name");
	}
	
	private static String getScript() {
		Scanner scanner = new Scanner(Main.class.getResourceAsStream(SCRIPT));
		scanner.useDelimiter("\\A");
		String contents = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		return contents;
	}
	
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Application started running.");
		
		if(!getOsVersion().startsWith("Mac OS")) {
			LOGGER.log(Level.SEVERE, "This application works only on MacOS!");
			return;
		}
		
		// Load the script from the .applescript file
		String script = getScript();
		
		// Create the AppleScriptHelper with the script
		AppleScriptHelper scriptHelper = new AppleScriptHelper(script);
		
		// Create the AppleScriptDiscordBridge
		AppleScriptDiscordBridge bridge = new AppleScriptDiscordBridge();
		
		// The bridge observes the script helper to receive updates
		// about the songs playing (in form of a String object).
		scriptHelper.addObserver(bridge);
		
		// Create the View (GUI/CLI element).
		View view = new MainFrame();
		
		// Create the DiscordHelper passing the MainFrame to it
		DiscordHelper discordHelper = new DiscordHelper(view);
		
		// The Discord helper observes the bridge to receive updates
		// about the songs playing (in form of a Track object).
		bridge.addObserver(discordHelper);
		
		// The script helper observes the Discord helper to be notified
		// when the script must be executed or stopped.
		discordHelper.setCommanded(scriptHelper);
		
		// Show the frame
		EventQueue.invokeLater(view::init);
		
		LOGGER.log(Level.INFO, "View invoked.");
	}
	
}
