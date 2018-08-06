package com.github.kevinmussi.itunesrp.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	public ScriptHelper(OperativeSystem os) throws IOException {
		File file = createTempFile(os);
		this.builder = new ProcessBuilder(os.getCommandName(), file.getAbsolutePath());
		this.process = null;
	}
	
	/**
	 * This method creates a temporary file in the default temporary-file
	 * directory containing the contents of the script to execute.
	 * This is done because the resources files in the packaged jar file
	 * can be loaded only as {@code InputStream} objects, so we need to
	 * first copy the contents of the stream to a temporary file, and then
	 * launch the script reading it from that file.
	 * 
	 * @param os
	 * @return the temporary file abstract reference.
	 * @throws IOException If the creation of the temporary file fails.
	 */
	private File createTempFile(OperativeSystem os) throws IOException {
		InputStream inputStream = ScriptHelper.class
				.getResourceAsStream(os.getScriptPath());
		File file = File.createTempFile("script", ".tmp");
		try(OutputStream outputStream = new FileOutputStream(file)) {
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			outputStream.write(buffer);
		}
		inputStream.close();
		file.deleteOnExit();
		return file;
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
