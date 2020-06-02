package com.github.kevinmussi.itunesrp.data;

public enum OperativeSystem {
	MACOS("MacOS", "macos", ".applescript", "osascript", "/Library/Application Support"),
	MACOS15("MacOS", "macos15", ".applescript", "osascript", "/Library/Application Support"),
	WINDOWS("Windows", "windows", ".js", "Cscript.exe", "/AppData/Local"),
	OTHER("Other", "", "", "", "");
	
	private static final String BASEFOLDER = "/scripts";
	private static final String SCRIPTNAME = "itunes_track_info_script";
	
	private final String description;
	private final String scriptPath;
	private final String scriptExtension;
	private final String commandName;
	private final String preferencesBaseFolder;
	
	private OperativeSystem(String description, String scriptPath, String scriptExtension, String commandName,
			String preferencesBaseFolder) {
		this.description = description;
		this.scriptPath = scriptPath;
		this.scriptExtension = scriptExtension;
		this.commandName = commandName;
		this.preferencesBaseFolder = preferencesBaseFolder;
	}
	
	public static OperativeSystem getOS() {
		String os = System.getProperty("os.name");
		if(os.startsWith("Mac OS")) {
			// Add check for OS X 10.15 or higher for the new music app
			String version = System.getProperty("os.version");
			int osxVersion = Integer.valueOf(version.split("\\.")[1]);
			if(osxVersion >= 15) {
				return MACOS15;
			} else {
				return MACOS;
			}
		} else if(os.startsWith("Windows")) {
			return WINDOWS;
		} else {
			return OTHER;
		}
	}
	
	public String getScriptPath() {
		if(this == OTHER) {
			return "";
		}
		return BASEFOLDER + "/" + scriptPath + "/" + SCRIPTNAME + scriptExtension;
	}
	
	public String getScriptExtension() {
		return scriptExtension;
	}
	
	public String getCommandName() {
		return commandName;
	}
	
	public String getPreferencesBaseFolder() {
		return preferencesBaseFolder;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
