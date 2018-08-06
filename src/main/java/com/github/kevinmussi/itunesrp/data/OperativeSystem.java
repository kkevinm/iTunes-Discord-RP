package com.github.kevinmussi.itunesrp.data;

public enum OperativeSystem {
	MACOS("MacOS", "macos", ".applescript", "osascript"),
	WINDOWS("Windows", "windows", ".js", "CScript.exe"),
	OTHER("Other", "", "", "");
	
	private static final String BASEFOLDER = "/scripts";
	private static final String SCRIPTNAME = "itunes_track_info_script";
	
	private final String description;
	private final String scriptPath;
	private final String scriptExtension;
	private final String commandName;
	
	private OperativeSystem(String description, String scriptPath,
			String scriptExtension, String commandName) {
		this.description = description;
		this.scriptPath = scriptPath;
		this.scriptExtension = scriptExtension;
		this.commandName = commandName;
	}
	
	public static OperativeSystem getOS() {
		String os = System.getProperty("os.name");
		if(os.startsWith("Mac OS")) {
			return MACOS;
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
	
	public String getCommandName() {
		return commandName;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
