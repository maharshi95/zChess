package com.zchess.pieces;

import java.io.IOException;
import java.util.ArrayList;

import com.zchess.gameplay.Color;
import com.zchess.movements.Position;

public class Rook extends Chessmen {

	public Rook(Color color) {
		color_var = color;
		name_var = "R";
		p_var = null;
		try {
			if(color_var == Color.BLACK)
				img = getImage("Black/Rook.png");
			else
				img = getImage("White/Rook.png");
		} catch (IOException e) {
			throw new RuntimeException("Rook Icon Not Found");
		}
	}

	@Override
	public int value() {
		return 5;
	}

	public ArrayList<Position> getValidMoves() {
		// TODO Auto-generated method stub
		return null;
	}

}
