package com.zchess.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import com.zchess.gameplay.Chessboard;
import com.zchess.movements.Move;
import com.zchess.movements.Position;
import acm.graphics.GCanvas;
import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.GPoint;

@SuppressWarnings("serial")
class GScreen extends GCanvas {
	
	Chessboard chessboard;
	GChessBoard gcboard;
	
	private double side;
	private HCell self = null;
	private HCell check = null;
	private double offset;
	public int H_OFFSET = 0;
	public int V_OFFSET = 0;
	
	private double gcboardSize;
	
	private double Xref;
	private double Yref;
	@SuppressWarnings("unused")
	private double x;
	@SuppressWarnings("unused")
	private double y;
	
	public GScreen(double width, double height, Chessboard board) {
		
		super();
		gcboardSize = Math.min(width, height) - 2;
		side = gcboardSize/8;
		this.setPreferredSize(new Dimension((int)width, (int)height));
		offset = side*0.15;
		chessboard = board;
		gcboard = new GChessBoard(gcboardSize);
		setBackground(GColors.SYSTEM_BG);
		
		x = (this.getPreferredSize().width - gcboardSize)/2;
		y = (this.getPreferredSize().height - gcboardSize)/2;
		
		Xref  = H_OFFSET;
		Yref  = V_OFFSET;
	}
	

	public void rePaint() {
		removeAll();
		add(gcboard, Xref, Yref );
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Position p = new Position(j,i);
				if(chessboard.isOccupied(p)) {
					GImage img = chessboard.chessmenAt(p).icon();
					img.setBounds(i*side + Xref, j*side + Yref, side, side);
					add(img);
				}
			}
		}
		if(check != null) {
			remove(check);
			check = null;
		}
	}
	
	public void showMoveHighLights(Position init, ArrayList<Move> moves) {
		self = (new HCell(side, true));
		add(self, point(init));
		if(check != null)
			add(check);
		Position p;
		for(Move m: moves) {
			p = m.finalPosition();
			HCell hr = new HCell(side, false);
			add(hr,p.getCol()*side + Xref , p.getRow()*side + Yref);
		}
		revalidate();
	}
	
	public void showAlertHighLight(Position kingPosition) {
		System.out.println(kingPosition.toString());
		check = new HCell(side);
		check.setLocation(point(kingPosition));
		add(check);
		revalidate();
	}
	
	public void hideAlertHighlight(Position k) {
		if(check != null) {
			System.out.println("removing cell");
			remove(check);
			check = null;
			revalidate();
		}
	}
	          	
	public void hideMoveHighlights() {
		remove(self);
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				GObject obj = getElementAt(j*side + Xref +offset , i*side + Yref + offset);
				if(obj instanceof HCell) 
					remove(obj);
			}
		}
		if(check != null)
			add(check);
		revalidate();
	}
	
	public double boardSize() {
		return gcboardSize;
	}
	
	public double cellSize() {
		return side;
	}
	
	public Position position(double x, double y) {
		int i = (int) ((y - Yref)/side);
		int j = (int) ((x - Xref)/side);
		return new Position(i,j);
	}
	
	//to be implemented
	public GPoint point(Position p) {
		return new GPoint(p.getCol()*side + Xref, p.getRow()*side + Yref);
	}
	
	public GPoint checkPoint(Position p) {
		return new GPoint(p.getCol()*side + Xref + offset, p.getRow()*side + Yref + offset);
	}
}
