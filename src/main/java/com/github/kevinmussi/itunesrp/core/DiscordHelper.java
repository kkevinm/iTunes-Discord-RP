package com.github.kevinmussi.itunesrp.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.github.kevinmussi.itunesrp.data.ScriptCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.gui.MainFrame;
import com.github.kevinmussi.itunesrp.observer.Observable;
import com.github.kevinmussi.itunesrp.observer.Observer;
import com.jagrosh.discordipc.IPCClient;

public class DiscordHelper
		extends Observable<ScriptCommand> implements Observer<Track> {
    
	private static final long APP_ID = 473069598804279309L;
	
	private final MainFrame frame;
	private IPCClient client;
	
    public DiscordHelper(MainFrame frame) {
    	this.frame = frame;
    	setWindowListener();
    	frame.addOnConnectButtonClickedListener(e -> connect());
    	frame.addOnDisconnectButtonClickedListener(e -> disconnect());
    }
    
    private void setWindowListener() {
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
		if(message == null) {
			return;
		}
		
	}
	
	private void connect() {
		notifyObservers(ScriptCommand.EXECUTE);
		frame.setConnected();
	}
	
	private void disconnect() {
		notifyObservers(ScriptCommand.KILL);
		frame.setDisconnected();
		if(client != null) {
			client.sendRichPresence(null);
			client.close();
		}
	}
	
	/*
	public static void main(String[] args) {
		try(IPCClient client = new IPCClient(APP_ID)) {
			client.setListener(new IPCListener() {
			    @Override
			    public void onReady(IPCClient client)
			    {
			        RichPresence.Builder builder = new RichPresence.Builder();
			        builder.setState("West of House")
			            .setDetails("Frustration level: Over 9000")
			            .setStartTimestamp(OffsetDateTime.now())
			            .setLargeImage("canary-large", "Discord Canary")
			            .setSmallImage("ptb-small", "Discord PTB")
			            .setParty("party1234", 1, 6)
			            .setMatchSecret("xyzzy")
			            .setJoinSecret("join")
			            .setSpectateSecret("look");
			        client.sendRichPresence(builder.build());
			    }
			});
			try {
				client.connect();
			} catch (NoDiscordClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
    
}
