package com.github.kevinmussi.itunesrp.view;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.kevinmussi.itunesrp.Main;
import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.observer.Commanded;
import com.github.kevinmussi.itunesrp.view.panels.ActivePanel;
import com.github.kevinmussi.itunesrp.view.panels.InactivePanel;

public class MainFrame extends View implements Commanded<ConnectCommand> {
	
	private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName() + "Logger");
	
	private static final String ICON_PATH = "/images/menu-bar/";
	
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
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		initTray();
	}
	
	private void initTray() {
		if(SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			String iconPath = ICON_PATH + Main.OS.getTrayIconName();
			InputStream imageStream = getClass().getResourceAsStream(iconPath);
			Image image;
			try {
				image = ImageIO.read(imageStream);
			} catch(IOException e) {
				throw new IllegalArgumentException("Something went wrong: ", e);
			}
			
			TrayIcon trayIcon = new TrayIcon(image, "iTunes Discord Rich Presence");
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					frame.setVisible(!frame.isVisible());
				}
			});
			try {
				tray.add(trayIcon);
			} catch(AWTException e) {
				LOGGER.log(Level.WARNING, "An error occurred while initializing tray icon: ", e);
			}
		}
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
			public void windowOpened(WindowEvent e) {
				/**/}
				
			@Override
			public void windowClosing(WindowEvent e) {
				/**/}
			
			@Override
			public void windowClosed(WindowEvent e) {
				sendCommand(ConnectCommand.DISCONNECT);
				e.getWindow().dispose();
			}
				
			@Override
			public void windowIconified(WindowEvent e) {
				/**/}
				
			@Override
			public void windowDeiconified(WindowEvent e) {
				/**/}
				
			@Override
			public void windowActivated(WindowEvent e) {
				/**/}
				
			@Override
			public void windowDeactivated(WindowEvent e) {
				/**/}
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
