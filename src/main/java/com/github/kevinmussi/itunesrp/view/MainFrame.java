package com.github.kevinmussi.itunesrp.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.data.Track;

public class MainFrame extends View {
	
	private static final Dimension DIMENSION = new Dimension(400, 255);
	private static final LineBorder LINE_BORDER = new LineBorder(new Color(184, 206, 227), 1);
	private static final Insets INSETS = new Insets(2, 10, 10, 10);
	private static final Font TEXT_FONT_BIG = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	private static final Font TEXT_FONT_SMALL = new Font(Font.SANS_SERIF, Font.BOLD, 13);
	
	/**
	 * String identifier of the active panel for the CardLayout.
	 */
	private static final String ACTIVE_PANEL = "active";
	
	/**
	 * String identifier of the inactive panel for the CardLayout.
	 */
	private static final String INACTIVE_PANEL = "inactive";
	
	private static final String WELCOME_TEXT =
			"To start using the application, make sure that you have the "
			+ "Discord app running and that you're logged in with your account. "
			+ "Then, you can just click the button below and wait for the connection "
			+ "to establish. After that, all the sick tunes played on iTunes "
			+ "will be shown here and on your Discord status!";
	
	/**
	 * The main frame of the GUI.
	 */
	private final JFrame frame;
	
	/**
	 * The CardLayout used to switch between the two JPanels.
	 */
	private final CardLayout cards;
	
	private final JButton connectButton;
	private final JButton disconnectButton;
	private final TrackPane trackPane;
	
	private boolean isConnected;
	private boolean didInit;
	
	public MainFrame() {
		this.didInit = false;
		this.isConnected = false;
		this.frame = new JFrame();
		this.disconnectButton = new JButton("Disconnect from Discord");
		this.connectButton = new JButton("Connect to Discord");
		this.trackPane = new TrackPane();
		this.cards = new CardLayout();
		initListeners();
	}
	
	@Override
	public void init() {
		if(didInit)
			return;
		
		didInit = true;
		
		// Panel to be shown when the application has successfully connected to Discord.
		JPanel activePanel = new JPanel();
		// Panel to be shown when the application has not been connected to Discord yet.
		JPanel inactivePanel = new JPanel();
		
		JPanel contentPane = new JPanel(cards);
		contentPane.add(inactivePanel, INACTIVE_PANEL);
		contentPane.add(activePanel, ACTIVE_PANEL);
		contentPane.setPreferredSize(DIMENSION);
		contentPane.setVisible(true);
		
		disconnectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		disconnectButton.setFont(TEXT_FONT_BIG);
		disconnectButton.setOpaque(false);
		disconnectButton.setVisible(true);
		
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		connectButton.setFont(TEXT_FONT_BIG);
		connectButton.setOpaque(false);
		connectButton.setVisible(true);
		
		activePanel.setBackground(Color.WHITE);
		activePanel.setLayout(new BoxLayout(activePanel, BoxLayout.Y_AXIS));
		trackPane.setMargin(INSETS);
		JScrollPane scrollPane = new JScrollPane(trackPane);
		TitledBorder border = BorderFactory.createTitledBorder("Song playing");
		border.setTitleFont(TEXT_FONT_SMALL);
		border.setBorder(LINE_BORDER);
		scrollPane.setBorder(border);
		activePanel.add(scrollPane);
		activePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		activePanel.add(disconnectButton);
		activePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JTextPane textPane = new JTextPane();
		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setFontFamily(attr, Font.SANS_SERIF);
		StyleConstants.setFontSize(attr, 16);
		textPane.setParagraphAttributes(attr, true);
		textPane.setText(WELCOME_TEXT);
		textPane.setEditable(false);
		textPane.setMargin(INSETS);
		textPane.setVisible(true);
		
		inactivePanel.setBackground(Color.WHITE);
		inactivePanel.setLayout(new BoxLayout(inactivePanel, BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(textPane);
		border = BorderFactory.createTitledBorder("Welcome!");
		border.setTitleFont(TEXT_FONT_SMALL);
		border.setBorder(LINE_BORDER);
		scrollPane.setBorder(border);
		inactivePanel.add(scrollPane);
		inactivePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		inactivePanel.add(connectButton);
		inactivePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		cards.show(contentPane, INACTIVE_PANEL);
		
		frame.setTitle("iTunes Rich Presence for Discord");
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showTrack(Track track) {
		if(isConnected) {
			trackPane.setTrack(track);
		}
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
			trackPane.setTrack(Track.NULL_TRACK);
			cards.show(frame.getContentPane(), INACTIVE_PANEL);
		}
	}
	
}
