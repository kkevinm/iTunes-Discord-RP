package com.github.kevinmussi.itunesrp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		URL resource = Main.class.getResource("/windows/itunes_track_info_script.js");
		File file;
		try {
			file = Paths.get(resource.toURI()).toFile();
			String path = file.getAbsolutePath();
			ProcessBuilder builder = new ProcessBuilder("Cscript.exe", path);
			//builder.redirectErrorStream(true);
			Process process;
			try {
				process = builder.start();
				//logger.log(Level.INFO, "The script started execution.");
				Scanner scanner = new Scanner(process.getErrorStream());
				scanner.useDelimiter("\n");
				while(process != null && process.isAlive()) {
					if(scanner.hasNext()) {
						//sendUpdate(scanner.next());
						System.out.println(scanner.next());
					}
					Thread.sleep(1000);
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
				//logger.log(Level.SEVERE, "The script did not execute correctly", e);
			} catch (InterruptedException e) {
				//logger.log(Level.WARNING, "An error occurred: ", e);
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*LOGGER.log(Level.INFO, "Application started running.");
		
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
		SwingUtilities.invokeLater(view::init);
		
		LOGGER.log(Level.INFO, "View invoked.");*/
	}
	
}
