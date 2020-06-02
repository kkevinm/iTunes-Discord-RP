package com.github.kevinmussi.itunesrp.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.commands.ScriptCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.observer.Commander;
import com.github.kevinmussi.itunesrp.observer.Observer;
import com.github.kevinmussi.itunesrp.view.View;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

public class DiscordHelper extends Commander<ScriptCommand> implements Observer<Track> {
	
	/**
	 * ClientId of my Discord application.
	 */
	private static final long APP_ID = 473069598804279309L;
	
	private static final String DISCORD_CONNECTION_ERROR_MESSAGE = "<html>An <b>error</b> occurred while trying to connect to <b>Discord</b>!<br>Make sure that:<br>"
			+ "<li>You have the Discord app installed and currently running.</li>"
			+ "<li>You're logged in with your account.</li></html>";
	private static final String DISCORD_ALREADY_DISCONNECTED_MESSAGE = "<html>The connection with <b>Discord</b> ended!</html>";
	
	private final Logger logger = Logger.getLogger(getClass().getName() + "Logger");
	
	private final View view;
	private final Commanded<ConnectCommand> connectObserver;
	private final IPCClient client;
	
	public DiscordHelper(View view) {
		this.view = view;
		this.connectObserver = new CommandReceiver();
		this.client = new IPCClient(APP_ID);
		
		client.setListener(new IPCListener() {
			@Override
			public void onDisconnect(IPCClient client, Throwable t) {
				// The connection with Discord ended, so we notify the view...
				if(view.isConnected()) {
					view.showDisconnected();
				}
				// ...terminate the script...
				sendCommand(ScriptCommand.KILL);
				// ...and show an alert.
				view.showMessage(DISCORD_ALREADY_DISCONNECTED_MESSAGE);
			}
		});
		
		// Observe the view to receive the ConnectCommands
		view.setCommanded(connectObserver);
	}
	
	@Override
	public void onUpdate(Track message) {
		logger.log(Level.INFO, "Received new track.");
		
		// Update the view
		SwingUtilities.invokeLater(() -> view.showTrack(message));
		
		// Update Discord RP
		if(message == null) {
			return;
		}
		if(message.isNull()) {
			client.sendRichPresence(null);
			return;
		}
		
		RichPresence rp = new RichPresenceBuilder(message).build();
		client.sendRichPresence(rp);
		
		logger.log(Level.INFO, "Updated Rich Presence.");
	}
	
	private class CommandReceiver implements Commanded<ConnectCommand> {
		
		@Override
		public boolean onCommand(ConnectCommand command) {
			if(command == null)
				return false;
			if(command == ConnectCommand.CONNECT)
				return connect();
			else
				return disconnect();
		}
		
		boolean connect() {
			try {
				client.connect();
			} catch(NoDiscordClientException | RuntimeException e) {
				logger.log(Level.SEVERE, "Something went wrong while trying to connect: {0}", e.getMessage());
				view.showMessage(DISCORD_CONNECTION_ERROR_MESSAGE);
				return false;
			}
			logger.log(Level.INFO, "Client successfully connected.");
			sendCommand(ScriptCommand.EXECUTE);
			return true;
		}
		
		private boolean disconnect() {
			sendCommand(ScriptCommand.KILL);
			try {
				client.close();
			} catch(IllegalStateException e) {
				logger.log(Level.INFO, "Client is already disconnected.");
				return true;
			}
			logger.log(Level.INFO, "Client successfully disconnected.");
			return true;
		}
		
	}
	
}
