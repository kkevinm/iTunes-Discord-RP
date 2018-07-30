package com.github.kevinmussi.itunesrp.core;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kevinmussi.itunesrp.commands.ScriptCommand;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.observer.Observable;

public class AppleScriptHelper
		extends Observable<String> implements Commanded<ScriptCommand> {
	
	public static final String TRACK_RECORD_SEPARATOR = ";;";
	
	private final Logger logger = Logger.getLogger(getClass().getName() + "Logger");
	
	private final ProcessBuilder builder;
	private volatile Process process;
	
	public AppleScriptHelper(String script) {
		this.builder = new ProcessBuilder("osascript", "-e", script);
		// The script logs its messages to stderr, so we need to redirect it to the stdout
		this.builder.redirectErrorStream(true);
		this.process = null;
	}
	
	@Override
	public boolean onCommand(ScriptCommand command) {
		if(command != null) {
			if(command == ScriptCommand.EXECUTE) {
				if(process == null || !process.isAlive()) {
					new Thread(this::executeScript).start();
					return true;
				}
				return false;
			} else {
				stopScript();
				return true;
			}
		}
		return false;
	}
	
	public void executeScript() {
		if(process != null && process.isAlive()) {
			return;
		}
		try {
			process = builder.start();
			logger.log(Level.INFO, "The script started execution.");
			Scanner scanner = new Scanner(process.getInputStream());
			scanner.useDelimiter("\n");
			while(process != null && process.isAlive()) {
				if(scanner.hasNext()) {
					sendUpdate(scanner.next());
				}
				Thread.sleep(1000);
			}
			scanner.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "The script did not execute correctly", e);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "An error occurred: ", e);
			Thread.currentThread().interrupt();
		}
	}
	
	public void stopScript() {
		if(process != null && process.isAlive()) {
			process.destroy();
			logger.log(Level.INFO, "The script stopped execution.");
		}
		process = null;
	}
	
}
