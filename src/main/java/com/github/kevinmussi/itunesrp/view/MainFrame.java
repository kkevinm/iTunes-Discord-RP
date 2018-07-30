package com.github.kevinmussi.itunesrp.view;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;

public class MainFrame extends View {
	
	private static final Dimension DIMENSION = new Dimension(400, 250);
	
	/**
	 * The main frame of the GUI.
	 */
	private final JFrame frame;
	
	/**
	 * Panel to be shown when the application is not
	 * yet connected to Discord.
	 */
	private final JPanel inactivePanel;
	
	/**
	 * Panel to be shown when the application has
	 * successfully connected to Discord.
	 */
	private final JPanel activePanel;
	
	private final JButton connectButton;
	private final JButton disconnectButton;
	
	private boolean didInit;
	
	public MainFrame() {
		this.didInit = false;
		this.frame = new JFrame();
		this.inactivePanel = new JPanel();
		this.activePanel = new JPanel();
		this.disconnectButton = new JButton("Disconnect from Discord");
		this.connectButton = new JButton("Connect to Discord");
		initListeners();
	}
	
	public void init() {
		if(didInit)
			return;
		
		didInit = true;
		JPanel contentPane = new JPanel();
		contentPane.add(inactivePanel);
		contentPane.add(activePanel);
		contentPane.setVisible(true);
		contentPane.setLayout(null);
		contentPane.setPreferredSize(DIMENSION);
		
		activePanel.setVisible(false);
		activePanel.setBounds(0, 0, DIMENSION.width, DIMENSION.height);
		
		disconnectButton.setOpaque(true);
		disconnectButton.setVisible(true);
		activePanel.add(disconnectButton);
		
		inactivePanel.setVisible(true);
		inactivePanel.setBounds(0, 0, DIMENSION.width, DIMENSION.height);
		
		connectButton.setOpaque(true);
		connectButton.setVisible(true);
		inactivePanel.add(connectButton);
		
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
			inactivePanel.setVisible(false);
			activePanel.setVisible(true);
		}
	}
	
	private void setDisconnected() {
		boolean didDisconnect = sendCommand(ConnectCommand.DISCONNECT);
		if(didDisconnect) {
			activePanel.setVisible(false);
			inactivePanel.setVisible(true);
		}
	}
	
	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
}
