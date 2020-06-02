package com.github.kevinmussi.itunesrp.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public final class PanelConstants {
	
	private static final Color myColor = new Color(184, 206, 227);
	
	public static final LineBorder LINE_BORDER = new LineBorder(myColor, 1);
	public static final CompoundBorder LIST_BORDER = BorderFactory
			.createCompoundBorder(new MatteBorder(0, 0, 1, 0, myColor), new EmptyBorder(5, 5, 5, 5));
	public static final EmptyBorder PADDING_BORDER = new EmptyBorder(0, 1, 1, 1);
	public static final Insets INSETS = new Insets(2, 10, 10, 10);
	public static final Font TEXT_FONT_BIG = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	public static final Font TEXT_FONT_SMALL = new Font(Font.SANS_SERIF, Font.BOLD, 13);
	public static final Font FONT_LIST = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	
	private PanelConstants() {
		super();
	}
	
}
