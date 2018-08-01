package com.github.kevinmussi.itunesrp.preferences;

import java.io.Serializable;

public class Preferences implements Serializable {
	
	private static final long serialVersionUID = -7150420841855273495L;
	
	private boolean autoConnect;
	
	Preferences() {
		super();
	}
	
	Preferences(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
	
	public static Preferences getDefault() {
		return new Preferences(false);
	}
	
	public boolean getAutoConnect() {
		return autoConnect;
	}
	
	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}
	
	public Preferences getCopy() {
		return new Preferences(autoConnect);
	}
	
}
