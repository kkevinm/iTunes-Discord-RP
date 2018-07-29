package com.github.kevinmussi.itunesrp.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -6959412269447126961L;
	
	private static final Dimension DIMENSION = new Dimension(400, 250);
	
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
	
	private boolean isConnected;
	private boolean didInit;
	
	public MainFrame() {
		this.didInit = false;
		this.isConnected = false;
		this.inactivePanel = new JPanel();
		this.activePanel = new JPanel();
		this.disconnectButton = new JButton("Disconnect from Discord");
		this.connectButton = new JButton("Connect to Discord");
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
		
		this.setContentPane(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void setConnected() {
		if(!isConnected) {
			isConnected = true;
			inactivePanel.setVisible(false);
			activePanel.setVisible(true);
		}
	}
	
	public void setDisconnected() {
		if(isConnected) {
			isConnected = false;
			activePanel.setVisible(false);
			inactivePanel.setVisible(true);
		}
	}
	
	public void addOnConnectButtonClickedListener(ActionListener listener) {
		this.connectButton.addActionListener(listener);
	}
	
	public void addOnDisconnectButtonClickedListener(ActionListener listener) {
		this.disconnectButton.addActionListener(listener);
	}
	
	public void showDialog(String title, String message, int messageType) {
		JOptionPane.showMessageDialog(null, message, title, messageType);
	}
	
}
