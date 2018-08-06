package com.github.kevinmussi.itunesrp;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.github.kevinmussi.itunesrp.core.DiscordHelper;
import com.github.kevinmussi.itunesrp.core.ScriptDiscordBridge;
import com.github.kevinmussi.itunesrp.core.ScriptHelper;
import com.github.kevinmussi.itunesrp.data.OperativeSystem;
import com.github.kevinmussi.itunesrp.view.MainFrame;
import com.github.kevinmussi.itunesrp.view.View;

public final class Main {
	
	private static final Logger LOGGER =
			Logger.getLogger(Main.class.getName() + "Logger");
	
	private Main() {
		super();
	}
	
	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Application started running.");
		
		// Get the OperativeSystem object
		OperativeSystem os = OperativeSystem.getOS();
		if(os == OperativeSystem.OTHER) {
			LOGGER.log(Level.SEVERE, "Your operative system is not supported!");
			return;
		}
		
		// Create the ScriptHelper
		ScriptHelper scriptHelper;
		try {
			scriptHelper = new ScriptHelper(os);
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Something went wrong: ", e);
			return;
		}
		
		// Create the ScriptDiscordBridge
		ScriptDiscordBridge bridge = new ScriptDiscordBridge();
		
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
		SwingUtilities.invokeLater(view::init);
		
		LOGGER.log(Level.INFO, "View invoked.");
	}
	
}
