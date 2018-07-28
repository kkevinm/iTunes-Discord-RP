package com.github.kevinmussi.itunesrp.applescript;

public final class AppleScripts {
	
	public static final String ITUNES_TRACK_INFO_SCRIPT;
	
	static {
		ITUNES_TRACK_INFO_SCRIPT = "set delimiter to \";;\"\n" +  
				"tell application \"iTunes\"\n" + 
					"set result to " +
						"artist of current track & delimiter & " +
						"name of current track & delimiter & " +
						"player position & delimiter & " +
						"duration of current track & delimiter &" +
						"player state\n" + 
					"return result\n" +
				"end tell";
	}
	
	private AppleScripts() {
		super();
	}
	
}
