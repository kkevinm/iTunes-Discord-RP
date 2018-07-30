package com.github.kevinmussi.itunesrp.observer;

public abstract class Commander<T extends Enum<T>> {
	
	private Commanded<T> commanded;
	
	public void setCommanded(Commanded<T> commanded) {
		this.commanded = commanded;
	}
	
	public void resetCommanded() {
		this.commanded = null;
	}
	
	public boolean sendCommand(T command) {
		if(commanded != null)
			return commanded.onCommand(command);
		return false;
	}
	
}
