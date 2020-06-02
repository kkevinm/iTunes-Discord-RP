package com.github.kevinmussi.itunesrp.view;

import static com.github.kevinmussi.itunesrp.view.panels.PanelConstants.TEXT_FONT_BIG;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import com.github.kevinmussi.itunesrp.data.FieldPosition;
import com.github.kevinmussi.itunesrp.preferences.Preferences;
import com.github.kevinmussi.itunesrp.preferences.PreferencesManager;

public class SettingsFrame {
	
	private static final int NUM_IMAGES = 4;
	private static final String IMAGE_BASE_PATH = "/images/itunes-logo/";
	private static final String[] FIELD_POSITION_LABELS = {"Top line", "Bottom line", "Don't show"};
	
	private final JFrame frame;
	private int selectedImage;
	private boolean emojiUsage;
	private FieldPosition artistPosition;
	private FieldPosition albumPosition;
	
	public SettingsFrame() {
		this.frame = new JFrame();
		SwingUtilities.invokeLater(this::init);
	}
	
	private void init() {
		Preferences pref = PreferencesManager.getPreferences();
		int currentImage = pref.getImageId();
		boolean useEmojis = pref.getUseEmojis();
		FieldPosition currentArtistPosition = pref.getArtistPosition();
		FieldPosition currentAlbumPosition = pref.getAlbumPosition();
		
		this.selectedImage = currentImage;
		this.emojiUsage = useEmojis;
		this.artistPosition = currentArtistPosition;
		this.albumPosition = currentAlbumPosition;
		
		JPanel mainPanel = new JPanel();
		frame.setContentPane(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10));
		
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
		JPanel imagePanelTop = new JPanel();
		imagePanelTop.setLayout(new GridLayout(2, 2));
		imagePanel.add(imagePanelTop);
		TitledBorder border = BorderFactory.createTitledBorder("Status image");
		border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		imagePanel.setBorder(border);
		
		ButtonGroup group = new ButtonGroup();
		for(int i = 0; i < NUM_IMAGES; i++) {
			ImageIcon icon = getCurrentIcon(i);
			JLabel label = new JLabel(icon);
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			JRadioButton button = new JRadioButton();
			panel.add(button);
			panel.add(label);
			final int n = i + 1;
			button.addActionListener(e -> setSelectedImage(n));
			if(i == currentImage - 1) {
				button.setSelected(true);
			}
			group.add(button);
			imagePanelTop.add(panel);
		}
		
		JRadioButton button = new JRadioButton("No image");
		button.setAlignmentX(Component.RIGHT_ALIGNMENT);
		if(currentImage == 0) {
			button.setSelected(true);
		}
		button.addActionListener(e -> setSelectedImage(0));
		group.add(button);
		imagePanel.add(button);
		
		JPanel emojiPanel = new JPanel();
		border = BorderFactory.createTitledBorder("Emoji usage");
		border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		emojiPanel.setBorder(border);
		emojiPanel.setLayout(new GridLayout(1, 2));
		group = new ButtonGroup();
		JRadioButton button1 = new JRadioButton("Enabled");
		JRadioButton button2 = new JRadioButton("Disabled");
		if(useEmojis) {
			button1.setSelected(true);
		} else {
			button2.setSelected(true);
		}
		button1.addActionListener(e -> setEmojiUsage(true));
		button2.addActionListener(e -> setEmojiUsage(false));
		group.add(button1);
		group.add(button2);
		emojiPanel.add(button1);
		emojiPanel.add(button2);
		
		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new BoxLayout(positionPanel, BoxLayout.X_AXIS));
		JPanel artistPanel = new JPanel();
		border = BorderFactory.createTitledBorder("Artist field");
		border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		artistPanel.setBorder(border);
		artistPanel.setLayout(new BoxLayout(artistPanel, BoxLayout.Y_AXIS));
		JPanel albumPanel = new JPanel();
		border = BorderFactory.createTitledBorder("Album field");
		border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		albumPanel.setBorder(border);
		albumPanel.setLayout(new BoxLayout(albumPanel, BoxLayout.Y_AXIS));
		positionPanel.add(artistPanel);
		positionPanel.add(albumPanel);
		
		ButtonGroup artistGroup = new ButtonGroup();
		ButtonGroup albumGroup = new ButtonGroup();
		for(int i = 0; i < 3; i++) {
			JRadioButton currentArtistButton = new JRadioButton(FIELD_POSITION_LABELS[i]);
			JRadioButton currentAlbumButton = new JRadioButton(FIELD_POSITION_LABELS[i]);
			artistGroup.add(currentArtistButton);
			albumGroup.add(currentAlbumButton);
			artistPanel.add(currentArtistButton);
			albumPanel.add(currentAlbumButton);
			if(i == currentArtistPosition.ordinal()) {
				currentArtistButton.setSelected(true);
			}
			if(i == currentAlbumPosition.ordinal()) {
				currentAlbumButton.setSelected(true);
			}
			final int n = i;
			currentArtistButton.addActionListener(e -> setArtistPosition(n));
			currentAlbumButton.addActionListener(e -> setAlbumPosition(n));
		}
		
		JButton saveButton = new JButton("Apply");
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.setFont(TEXT_FONT_BIG);
		saveButton.setOpaque(false);
		saveButton.setVisible(true);
		saveButton.addActionListener(e -> saveSettings());
		
		mainPanel.add(imagePanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(emojiPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(positionPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(saveButton);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		frame.setTitle("Settings");
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private ImageIcon getCurrentIcon(int i) {
		int imageId = i + 1;
		String imagePath = IMAGE_BASE_PATH + "itunes-logo-" + imageId + ".png";
		InputStream imageStream = getClass().getResourceAsStream(imagePath);
		ImageIcon icon;
		try {
			icon = new ImageIcon(ImageIO.read(imageStream));
		} catch(IOException e) {
			throw new IllegalArgumentException("Something went wrong: ", e);
		}
		return icon;
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	private void setSelectedImage(int i) {
		this.selectedImage = i;
	}
	
	private void setEmojiUsage(boolean emojiUsage) {
		this.emojiUsage = emojiUsage;
	}
	
	private void setArtistPosition(int i) {
		this.artistPosition = FieldPosition.values()[i];
	}
	
	private void setAlbumPosition(int i) {
		this.albumPosition = FieldPosition.values()[i];
	}
	
	private void saveSettings() {
		Preferences pref = PreferencesManager.getPreferences();
		pref.setImageId(this.selectedImage);
		pref.setUseEmojis(this.emojiUsage);
		pref.setArtistPosition(this.artistPosition);
		pref.setAlbumPosition(this.albumPosition);
		PreferencesManager.setPreferences(pref);
	}
	
}
