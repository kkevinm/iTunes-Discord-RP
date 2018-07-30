package com.github.kevinmussi.itunesrp.observer;

public interface Commanded<T extends Enum<T>> {
	
	public boolean onCommand(T command);
	
}
