package com.github.kevinmussi.itunesrp.view;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.observer.Commander;

public abstract class View extends Commander<ConnectCommand> {
	
	public abstract void init();
	
	public abstract void showMessage(String message);
	
}
