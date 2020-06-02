package com.github.kevinmussi.itunesrp.view.panels;

import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.observer.Commander;
import com.github.kevinmussi.itunesrp.preferences.Preferences;
import com.github.kevinmussi.itunesrp.preferences.PreferencesManager;
import com.github.kevinmussi.itunesrp.view.Panel;

public class InactivePanel extends Commander<ConnectCommand> implements Panel {
	
	private static final String WELCOME_TEXT = "To start using the application, make sure that you have the "
			+ "Discord app running and that you're logged in with your account. "
			+ "Then, you can just click the button below and wait for the connection "
			+ "to establish. After that, all the sick tunes played on iTunes "
			+ "will be shown here and on your Discord status!";
	
	private final JPanel panel;
	private final JCheckBox checkBox;
	
	public InactivePanel() {
		this.panel = new JPanel();
		this.checkBox = new JCheckBox("Connect me automatically");
	}
	
	@Override
	public void init() {
		JButton connectButton = new JButton("Connect to Discord");
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		connectButton.setFont(TEXT_FONT_BIG);
		connectButton.setOpaque(false);
		connectButton.setVisible(true);
		connectButton.addActionListener(e -> {
			boolean autoConnect = checkBox.isSelected();
			Preferences prefs = PreferencesManager.getPreferences();
			// Update the preferences when the user clicks the checkbox
			if(prefs.getAutoConnect() != autoConnect) {
				prefs.setAutoConnect(autoConnect);
				PreferencesManager.setPreferences(prefs);
			}
			sendCommand(ConnectCommand.CONNECT);
		});
		
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
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		TitledBorder border = BorderFactory.createTitledBorder("Welcome!");
		border.setTitleFont(TEXT_FONT_SMALL);
		border.setBorder(LINE_BORDER);
		scrollPane.setBorder(border);
		scrollPane.setBackground(Color.WHITE);
		
		boolean autoConnect = PreferencesManager.getPreferences().getAutoConnect();
		checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		checkBox.setSelected(autoConnect);
		checkBox.setOpaque(false);
		
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(scrollPane);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(connectButton);
		panel.add(checkBox);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		if(autoConnect) {
			// If autoConnect is "true", connect and show the right panel
			sendCommand(ConnectCommand.CONNECT);
		}
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
}
