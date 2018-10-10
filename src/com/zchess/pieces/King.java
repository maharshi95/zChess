package com.zchess.pieces;

import java.io.IOException;

import com.zchess.gameplay.Color;

public class King extends Chessmen {

	public King(Color color) {
		color_var = color;
		name_var = "K";
		p_var = null;
		try {
			if(color_var == Color.BLACK)
				img = getImage("Black/King.png");
			else
				img = getImage("White/King.png");
		} catch (IOException e) {
			throw new RuntimeException("King Icon Not Found");
		}
	}

	@Override
	public int value() {
		return 1;
	}


}
