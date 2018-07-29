package com.github.kevinmussi.itunesrp.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.github.kevinmussi.itunesrp.data.ScriptCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.gui.MainFrame;
import com.github.kevinmussi.itunesrp.observer.Observable;
import com.github.kevinmussi.itunesrp.observer.Observer;
import com.github.kevinmussi.itunesrp.util.Pair;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

public class DiscordHelper
		extends Observable<ScriptCommand> implements Observer<Track> {
    
	private static final long APP_ID = 473069598804279309L;
	
	private static final String DISCORD_CONNECTION_ERROR_MESSAGE =
			"An error occurred while trying to connect to Discord.\n"
			+ "Make sure that you have the Discord app installed and "
			+ "that you're logged in with your account.";
	
	private final Logger logger = Logger.getLogger(getClass().getSimpleName() + "Logger");
	
	private final MainFrame frame;
	private final IPCClient client;
	
    public DiscordHelper(MainFrame frame) {
    	this.frame = frame;
    	this.client = new IPCClient(APP_ID);
    	setListeners();
    	frame.addOnConnectButtonClickedListener(e -> connect());
    	frame.addOnDisconnectButtonClickedListener(e -> disconnect());
    }
    
    private void setListeners() {
    	frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {/**/}
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				e.getWindow().dispose();
			}
			@Override
			public void windowClosed(WindowEvent e) {/**/}
			@Override
			public void windowIconified(WindowEvent e) {/**/}
			@Override
			public void windowDeiconified(WindowEvent e) {/**/}
			@Override
			public void windowActivated(WindowEvent e) {/**/}
			@Override
			public void windowDeactivated(WindowEvent e) {/**/}
    	});
    }

	@Override
	public void update(Track message) {
		logger.log(Level.INFO, () -> LocalDateTime.now().toString() + " Received new track.");
		if(message == null) {
			return;
		}
		if(message.isNull()) {
			resetRichPresence();
			return;
		}
		
		RichPresence.Builder builder = new RichPresence.Builder();
		if(message.getState() == TrackState.PLAYING) {
			Pair<OffsetDateTime, OffsetDateTime> times =
					getTimestamps(message.getCurrentPosition(), message.getDuration());
			builder.setStartTimestamp(times.first);
			builder.setEndTimestamp(times.second);
			builder.setDetails("Currently playing: " + message.getName());
			builder.setInstance(true);
		} else {
			builder.setDetails("Currently paused: " + message.getName());
		}
		builder.setState("By: " + message.getArtist());
		builder.setLargeImage(message.getApplication().getImageKey(),
				message.getApplication().toString());
		client.sendRichPresence(builder.build());
		logger.log(Level.INFO, () -> LocalDateTime.now().toString() + " Updated Rich Presence.");
	}
	
	private void connect() {
		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			logger.log(Level.INFO, e.getMessage(), e);
			frame.showDialog("Error",
					DISCORD_CONNECTION_ERROR_MESSAGE,
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		logger.log(Level.INFO, () -> LocalDateTime.now().toString() + " Client successfully connected.");
		notifyObservers(ScriptCommand.EXECUTE);
		frame.setConnected();
	}
	
	private void disconnect() {
		notifyObservers(ScriptCommand.KILL);
		frame.setDisconnected();
		if(client != null) {
			resetRichPresence();
			client.close();
		}
		logger.log(Level.INFO, () -> LocalDateTime.now().toString() + " Client successfully disconnected.");
	}
	
	private void resetRichPresence() {
		client.sendRichPresence(null);
	}
	
	private Pair<OffsetDateTime, OffsetDateTime>
			getTimestamps(double currentPosition, double duration) {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime start = now.minusNanos((long)(currentPosition*1e+9));
		OffsetDateTime end = start.plusNanos((long)(duration*1e+9));
		return new Pair<>(start, end);
	}
    
}
