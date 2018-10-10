package com.zchess.pieces;

import java.io.IOException;
import java.util.ArrayList;

import com.zchess.gameplay.Color;
import com.zchess.movements.Position;

public class Queen extends Chessmen {

	public Queen(Color color) {
		color_var = color;
		name_var = "Q";
		p_var = null;
		try {
			if(color_var == Color.BLACK)
				img = getImage("Black/Queen.png");
			else
				img = getImage("White/Queen.png");
		} catch (IOException e) {
			throw new RuntimeException("Queen Icon Not Found");
		}
	}

	@Override
	public int value() {
		return 9;
	}

	public ArrayList<Position> getValidMoves() {
		// TODO Auto-generated method stub
		return null;
	}

}
