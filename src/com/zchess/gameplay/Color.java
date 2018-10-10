package com.zchess.gameplay;


@SuppressWarnings("serial")
public class Color extends java.awt.Color {

	private String name;
	public Color(java.awt.Color color, String name) {
		super(color.getRGB());
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public static final Color WHITE = new Color(java.awt.Color.WHITE,  "W");
	public static final Color BLACK = new Color(java.awt.Color.WHITE,  "B");
}
