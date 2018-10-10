package com.zchess.pieces;

import java.io.IOException;

import com.zchess.gameplay.Color;

public class Bishop extends Chessmen {

	public Bishop(Color color) {
		color_var = color;
		name_var = "B";
		p_var = null;
		try {
			if(color_var == Color.BLACK)
				img = getImage("Black/Bishop.png");
			else
				img = getImage("White/Bishop.png");
		} catch (IOException e) {
			throw new RuntimeException("Bishop Icon Not Found");
		}
	}

	@Override
	public int value() {
		return 3;
	}


}
