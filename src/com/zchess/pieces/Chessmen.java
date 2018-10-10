package com.zchess.pieces;

import java.io.IOException;
import java.util.ArrayList;

import com.zchess.gameplay.Color;
import com.zchess.movements.InvalidPositionException;
import com.zchess.movements.Position;
import acm.graphics.GImage;

public abstract class Chessmen {
	
	public static final String PIECE_URL = "resource/ChessPiece/";
	
	protected Color color_var;
	protected String name_var;
	protected Position p_var;
	protected GImage img;
	
	
	public static final GImage getImage(String url) throws IOException {
		return new GImage(PIECE_URL + url);
	}
	
	public Color color() {
		return color_var;
	}

	public String name() {
		return name_var;
	}
	
	public Position position() {
		return p_var;
	}
	
	public GImage icon() {
		return img;
	}
	
	
	public void placeAt(Position p) {
		p_var = p;
	}
	
	public void placeAt(String pos) throws InvalidPositionException {
		p_var = new Position(pos);
	}
	
	public void assignPosition(Position p) {
		placeAt(p);
	}
	
	public void die() {
		p_var = null;
	}
	
	
	
	public char file() {
		return p_var.getFile();
	}
	
	public int rank() {
		return p_var.getRank();
	}
	
	public int row() {
		return p_var.getRow();
	}
	
	public int col() {
		return p_var.getCol();
	}
	
	
	
	public boolean isKing() {
		return name_var.equals("K");
	}
	
	
	public boolean isWhite() {
		return color_var == Color.WHITE;
	}
	
	public boolean isBlack() {
		return color_var == Color.BLACK;
	}
	
	public boolean isAlive() {
		return p_var != null;
	}
	
	public boolean isDead() {
		return p_var == null;
	}
	
	public boolean isOnboard() {
		return p_var != null;
	}
	
	
	public boolean isEnemy(Chessmen piece) {
		return (piece.color() != color_var);
	}
	
	public boolean isAlly(Chessmen piece) {
		return !isEnemy(piece);
	}
	
	public boolean canMoveTo(Position p) {
		return (getValidMoves().contains(p));
	}
	//to be edited
	private ArrayList<Position> getValidMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canMoveTo(String pos) throws InvalidPositionException {
		Position p = new Position(pos);
		return (getValidMoves().contains(p));
	}
	
	public String toString() {
		
		String str = color_var.toString() + " " + name_var + " ";
		if(isOnboard())
			str += position().toString();
		else
			str += "XX";
		return str;
	}
	
	public abstract int value();
	
	//public abstract boolean isUnderAttack();
	
	
}
