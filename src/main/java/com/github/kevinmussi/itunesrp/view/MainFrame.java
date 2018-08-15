package com.github.kevinmussi.itunesrp.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.view.panels.ActivePanel;
import com.github.kevinmussi.itunesrp.view.panels.InactivePanel;

public class MainFrame extends View implements Commanded<ConnectCommand> {
	
	private static final Dimension DIMENSION = new Dimension(400, 255);
	
	/**
	 * String identifier of the active panel for the CardLayout.
	 */
	private static final String ACTIVE_PANEL = "active";
	
	/**
	 * String identifier of the inactive panel for the CardLayout.
	 */
	private static final String INACTIVE_PANEL = "inactive";
	
	/**
	 * The main frame of the GUI.
	 */
	private final JFrame frame;
	
	/**
	 * The CardLayout used to switch between the two JPanels.
	 */
	private final CardLayout cards;
	
	private final InactivePanel inactivePanel;
	private final ActivePanel activePanel;
	
	private boolean isConnected;
	private boolean didInit;
	
	public MainFrame() {
		this.didInit = false;
		this.isConnected = false;
		this.frame = new JFrame();
		this.inactivePanel = new InactivePanel();
		this.activePanel = new ActivePanel();
		this.cards = new CardLayout();
		initListeners();
	}
	
	@Override
	public void init() {
		if(didInit)
			return;
		
		didInit = true;
		
		JPanel contentPane = new JPanel(cards);
		frame.setContentPane(contentPane);
		contentPane.add(inactivePanel.getPanel(), INACTIVE_PANEL);
		contentPane.add(activePanel.getPanel(), ACTIVE_PANEL);
		contentPane.setPreferredSize(DIMENSION);
		contentPane.setVisible(true);
		cards.show(contentPane, INACTIVE_PANEL);
		
		inactivePanel.setCommanded(this);
		activePanel.setCommanded(this);
		inactivePanel.init();
		activePanel.init();
		
		frame.setTitle("iTunes Rich Presence for Discord");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	@Override
	public boolean isConnected() {
		return isConnected;
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showTrack(Track track) {
		if(isConnected) {
			activePanel.setTrack(track);
		}
	}
	
	@Override
	public boolean onCommand(ConnectCommand command) {
		if(command == null) {
			return false;
		} else if(command == ConnectCommand.CONNECT) {
			return setConnected();
		} else {
			return setDisconnected();
		}
	}
	
	@Override
	public void showConnected() {
		isConnected = true;
		cards.show(frame.getContentPane(), ACTIVE_PANEL);
	}

	@Override
	public void showDisconnected() {
		isConnected = false;
		activePanel.setTrack(Track.NULL_TRACK);
		cards.show(frame.getContentPane(), INACTIVE_PANEL);
	}
	
	private void initListeners() {
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {/**/}
			@Override
			public void windowClosing(WindowEvent e) {
				sendCommand(ConnectCommand.DISCONNECT);
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
	
	private boolean setConnected() {
		boolean didConnect = sendCommand(ConnectCommand.CONNECT);
		if(didConnect) {
			showConnected();
		}
		return didConnect;
	}
	
	private boolean setDisconnected() {
		boolean didDisconnect = sendCommand(ConnectCommand.DISCONNECT);
		if(didDisconnect) {
			showDisconnected();
		}
		return didDisconnect;
	}
    
}
