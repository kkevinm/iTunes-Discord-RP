package com.github.kevinmussi.itunesrp.view.panels;

import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.FONT_LIST;
import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.INSETS;
import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.LINE_BORDER;
import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.LIST_BORDER;
import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.PADDING_BORDER;
import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.TEXT_FONT_BIG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;

import com.github.kevinmussi.itunesrp.commands.ConnectCommand;
import com.github.kevinmussi.itunesrp.data.Track;
import com.github.kevinmussi.itunesrp.data.TrackState;
import com.github.kevinmussi.itunesrp.observer.Commander;
import com.github.kevinmussi.itunesrp.view.Panel;
import com.github.kevinmussi.itunesrp.view.SettingsFrame;
import com.github.kevinmussi.itunesrp.view.TrackPane;

public class ActivePanel extends Commander<ConnectCommand> implements Panel {
	
	private final JPanel panel;
	private final TrackPane trackPane;
	private final DefaultListModel<Track> listModel;
	private SettingsFrame settingsFrame;
	
	public ActivePanel() {
		this.panel = new JPanel();
		this.trackPane = new TrackPane();
		this.trackPane.setMargin(INSETS);
		this.listModel = new DefaultListModel<>();
	}
	
	@Override
	public void init() {
		JPanel trackPanel = new JPanel();
		JPanel listPanel = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Song playing", trackPanel);
		tabbedPane.addTab("History", listPanel);
		
		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		disconnectButton.setFont(TEXT_FONT_BIG);
		disconnectButton.setOpaque(false);
		disconnectButton.setVisible(true);
		disconnectButton.addActionListener(e -> sendCommand(ConnectCommand.DISCONNECT));
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		settingsButton.setFont(TEXT_FONT_BIG);
		settingsButton.setOpaque(false);
		settingsButton.setVisible(true);
		settingsButton.addActionListener(e -> showSettings());
		
		JScrollPane scrollPane = new JScrollPane(trackPane);
		scrollPane.setBorder(LINE_BORDER);
		trackPanel.setLayout(new BorderLayout());
		trackPanel.add(scrollPane, BorderLayout.CENTER);
		trackPanel.setBorder(PADDING_BORDER);
		
		JList<Track> trackList = new JList<>(listModel);
		trackList.setCellRenderer(new MyCellRenderer(LIST_BORDER, FONT_LIST));
		scrollPane = new JScrollPane(trackList);
		scrollPane.setBorder(LINE_BORDER);
		listPanel.setBorder(PADDING_BORDER);
		listPanel.setLayout(new BorderLayout());
		listPanel.add(scrollPane, BorderLayout.CENTER);
		
		Box buttons = Box.createHorizontalBox();
		buttons.add(disconnectButton);
		buttons.add(settingsButton);
		
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(tabbedPane);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttons);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	public void setTrack(Track track) {
		if(track != null) {
			trackPane.setTrack(track);
			if(track.getState() == TrackState.PLAYING) {
				// Only show the played songs in the history
				listModel.add(0, track);
			}
		}
	}
	
	public void showSettings() {
		if(settingsFrame == null) {
			settingsFrame = new SettingsFrame();
		} else {
			settingsFrame.setVisible(true);
		}
	}
	
	private class MyCellRenderer extends JTextArea implements ListCellRenderer<Track> {
		
		private static final long serialVersionUID = 7705270732207459385L;
		
		public MyCellRenderer(AbstractBorder border, Font font) {
			this.setBorder(border);
			this.setFont(font);
			this.setEditable(false);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Track> list, Track value, int index,
				boolean isSelected, boolean cellHasFocus) {
			this.setText(value.toString());
			return this;
		}
		
	}
	
}
