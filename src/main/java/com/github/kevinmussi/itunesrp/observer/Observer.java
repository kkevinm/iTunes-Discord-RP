package com.github.kevinmussi.itunesrp.observer;

@FunctionalInterface
public interface Observer<T> {
	
	public void onUpdate(T message);
	
}
