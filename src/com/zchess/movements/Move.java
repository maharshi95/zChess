package com.zchess.movements;

public class Move {
	
	private Position p_init;
	private Position p_fin;
	private String promotion;
	
	public Move(Position p_before, Position p_after, String pname) {
		this.p_init = p_before;
		this.p_fin = p_after;
		this.promotion = pname;
	}
	
	public Move(Position p_before, Position p_after) {
		this(p_before, p_after, " ");
	}
	
	public Move(String p1, String p2) {
		p_init = new Position(p1);
		p_fin = new Position(p2);
		promotion = " ";
	}
	
	public void setPromotion(String pname) {
		promotion = pname;
	}
	public Position initialPosition() {
		return p_init;
	}
	
	public Position finalPosition() {
		return p_fin;
	}
	
	public String promoType() {
		return promotion;
	}
	
	public boolean equals(Object move) {
		return p_init.equals(((Move) move).initialPosition()) 
				&& p_fin.equals(((Move) move).finalPosition())
				&& promotion.equals(((Move)move).promoType());
	}
	
	public String toString() {
		String str = "[" + p_init.toString() + " " + p_fin.toString() +promotion + "]";
		return str;
	}
	
	
}
