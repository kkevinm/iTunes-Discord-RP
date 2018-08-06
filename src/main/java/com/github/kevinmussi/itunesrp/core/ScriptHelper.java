package com.github.kevinmussi.itunesrp.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.kevinmussi.itunesrp.commands.ScriptCommand;
import com.github.kevinmussi.itunesrp.data.OperativeSystem;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.observer.Observable;

public class ScriptHelper
		extends Observable<String> implements Commanded<ScriptCommand> {
	
	public static final String TRACK_RECORD_SEPARATOR = ";;";
	
	private final Logger logger = Logger.getLogger(getClass().getName() + "Logger");
	
	private final ProcessBuilder builder;
	private volatile Process process;
	
	public ScriptHelper(OperativeSystem os) throws URISyntaxException {
		String absolutePath = Paths
				.get(ScriptHelper.class
						.getResource(os.getScriptPath())
						.toURI())
				.toFile()
				.getAbsolutePath();
		this.builder = new ProcessBuilder(os.getCommandName(), absolutePath);
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
			// The script logs its messages to stderr
			Scanner scanner = new Scanner(process.getErrorStream());
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
