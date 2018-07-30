package com.github.kevinmussi.itunesrp.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.data.Track;

public class MainFrame extends View {
	
	private static final Dimension DIMENSION = new Dimension(400, 200);
	private static final String ACTIVE_PANEL = "active";
	private static final String INACTIVE_PANEL = "inactive";
	
	/**
	 * The main frame of the GUI.
	 */
	private final JFrame frame;
	
	/**
	 * The CardLayout used to switch
	 * between the two JPanels.
	 */
	private final CardLayout cards;
	
	private final JButton connectButton;
	private final JButton disconnectButton;
	private final TrackTextArea textArea;
	
	private boolean isConnected;
	private boolean didInit;
	
	public MainFrame() {
		this.didInit = false;
		this.isConnected = false;
		this.frame = new JFrame();
		this.disconnectButton = new JButton("Disconnect from Discord");
		this.connectButton = new JButton("Connect to Discord");
		this.textArea = new TrackTextArea();
		this.cards = new CardLayout();
		initListeners();
	}
	
	public void init() {
		if(didInit)
			return;
		
		didInit = true;
		
		// Panel to be shown when the application has
		// successfully connected to Discord.
		JPanel activePanel = new JPanel();
		// Panel to be shown when the application is not
		// yet connected to Discord.
		JPanel inactivePanel = new JPanel();
		
		JPanel contentPane = new JPanel(cards);
		contentPane.add(inactivePanel, INACTIVE_PANEL);
		contentPane.add(activePanel, ACTIVE_PANEL);
		contentPane.setVisible(true);
		
		activePanel.setPreferredSize(DIMENSION);
		
		disconnectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		disconnectButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		disconnectButton.setOpaque(true);
		disconnectButton.setVisible(true);
		
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		connectButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		connectButton.setOpaque(true);
		connectButton.setVisible(true);
		
		activePanel.setLayout(new BoxLayout(activePanel, BoxLayout.Y_AXIS));
		activePanel.add(textArea);
		activePanel.add(Box.createVerticalGlue());
		activePanel.add(disconnectButton);
		
		inactivePanel.add(connectButton);
		
		cards.show(contentPane, INACTIVE_PANEL);
		
		frame.setTitle("iTunes Rich Presence for Discord");
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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
		connectButton.addActionListener(e -> setConnected());
		disconnectButton.addActionListener(e -> setDisconnected());
	}
	
	private void setConnected() {
		boolean didConnect = sendCommand(ConnectCommand.CONNECT);
		if(didConnect) {
			isConnected = true;
			cards.show(frame.getContentPane(), ACTIVE_PANEL);
		}
	}
	
	private void setDisconnected() {
		boolean didDisconnect = sendCommand(ConnectCommand.DISCONNECT);
		if(didDisconnect) {
			isConnected = false;
			textArea.setTrack(Track.NULL_TRACK);
			cards.show(frame.getContentPane(), INACTIVE_PANEL);
		}
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showTrack(Track track) {
		if(isConnected) {
			textArea.setTrack(track);
		}
	}
	
}
