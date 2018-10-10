package com.zchess.pieces;

import java.io.*;

import com.zchess.gameplay.Color;
import com.zchess.gameplay.*;
import com.zchess.movements.*;

@SuppressWarnings("unused")
public class Pawn extends Chessmen {

	public Pawn(Color color) {
		color_var = color;
		name_var = "P";
		p_var = null;
		try {
			if(color_var == Color.BLACK)
				img = getImage("Black/Pawn.png");
			else
				img = getImage("White/Pawn.png");
		} catch (IOException e) {
			throw new RuntimeException("Pawn Icon Not Found");
		}
	}


	@Override
	public int value() {
		return 1;
	}

	public boolean canPromote() {
		return false;
		
	}

}
