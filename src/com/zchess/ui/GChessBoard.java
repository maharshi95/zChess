package com.zchess.ui;

import com.zchess.movements.Position;
import acm.graphics.GCompound;
import acm.graphics.GRect;

public class GChessBoard extends GCompound {

	public GChessBoard(double length) {
		double side = length/8;
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				GRect cell = new GRect(side, side);
				Position p = new Position(j,i);
				if(p.isBlack()) {
					cell.setFillColor(GColors.CELL_DARK);
				}
				else {
					cell.setFillColor(GColors.CELL_LIGHT);
				}
				cell.setFilled(true);
				add(cell, i*side, j*side);
			}
		}
		setVisible(true);
		markAsComplete();
	}
}
