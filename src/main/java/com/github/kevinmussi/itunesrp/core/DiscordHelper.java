package com.github.kevinmussi.itunesrp.core;

import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.commands.ScriptCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.observer.Commander;
import com.github.kevinmussi.itunesrp.observer.Observer;
import com.github.kevinmussi.itunesrp.view.View;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

public class DiscordHelper
		extends Commander<ScriptCommand> implements Observer<Track> {
    
	private static final long APP_ID = 473069598804279309L;
	
	private static final String DISCORD_CONNECTION_ERROR_MESSAGE =
			"<html>An <b>error</b> occurred while trying to connect to <b>Discord</b>.<br>Make sure that:<br>"
			+ "<li>You have the Discord app installed and currently running.</li>"
			+ "<li>You're logged in with your account.</li></html>";
	
	private final Logger logger = Logger.getLogger(getClass().getName() + "Logger");
	
	private final View view;
	private final Commanded<ConnectCommand> connectObserver;
	private final IPCClient client;
	
    public DiscordHelper(View view) {
    	this.view = view;
    	this.connectObserver = new CommandReceiver();
    	this.client = new IPCClient(APP_ID);
    	
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
			resetRichPresence();
			return;
		}
		
		RichPresence.Builder builder = new RichPresence.Builder();
		if(message.getState() == TrackState.PLAYING) {
			OffsetDateTime start = OffsetDateTime.now()
					.minusSeconds((long) message.getCurrentPosition());
			builder.setStartTimestamp(start);
			builder.setInstance(true);
		}
		builder.setDetails("🎶 " + message.getName());
		String artist = message.getArtist();
		String album = message.getAlbum();
		
		// Fix the fields' length to make everything stay in one line
		int max = 46;
		if(artist.length() > 27) {
			artist = artist.substring(0, 26) + "...";
			max = 50;
		}
		if(artist.length() + album.length() > max) {
			album = album.substring(0, max-artist.length()) + "...";
		}
		
		builder.setState("👤 " + artist + " 💿 " + album);
		String state = message.getState().toString();
		builder.setSmallImage(state.toLowerCase(), state);
		builder.setLargeImage(message.getApplication().getImageKey(),
				message.getApplication().toString());
		int index = message.getIndex();
		int size = message.getAlbumSize();
		if(index > 0 && size > 0 && index <= size) {
			builder.setParty("aa", index, size);
		}
		client.sendRichPresence(builder.build());
		logger.log(Level.INFO, "Updated Rich Presence.");
	}
	
	private void resetRichPresence() {
		client.sendRichPresence(null);
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
		
		private boolean connect() {
			try {
				client.connect();
			} catch (NoDiscordClientException|RuntimeException e) {
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
				resetRichPresence();
				client.close();
			} catch(IllegalStateException e) {
				logger.log(Level.INFO, "Client is already disconnected.");
				return false;
			}
			logger.log(Level.INFO, "Client successfully disconnected.");
			return true;
		}
		
	}
    
}
