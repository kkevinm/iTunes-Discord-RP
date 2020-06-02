package com.github.kevinmussi.itunesrp.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
	
	private final List<Observer<T>> observers = new ArrayList<>();
	
	public void addObserver(Observer<T> observer) {
		synchronized(observers) {
			observers.add(observer);
		}
	}
	
	public void removeObserver(Observer<T> observer) {
		synchronized(observers) {
			observers.remove(observer);
		}
	}
	
	public void sendUpdate(T message) {
		synchronized(observers) {
			for(Observer<T> observer : observers) {
				observer.onUpdate(message);
			}
		}
	}
	
}
