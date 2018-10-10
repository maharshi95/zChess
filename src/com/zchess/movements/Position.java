package com.zchess.movements;

import java.util.*;

import com.zchess.gameplay.Color;
import com.zchess.gameplay.*;

public class Position {
	
	private char file;
	private int rank;
	
	 /* Constructors */
	public Position(char pfile, int prank) throws InvalidPositionException {
		if(isValid(pfile, prank)) {
			file = pfile;
			rank = prank;
		}
		else {
			System.out.println("Invalid position: " + file + " " + rank );
			throw new InvalidPositionException();
		}
	}
	
	public Position(String pos) throws InvalidPositionException {
		if(isValid(pos)) {
			file = pos.charAt(0);
			rank = pos.charAt(1) - '0';
		}
		else throw new InvalidPositionException();
	}
	
	public Position(int row, int col) {
		if(isValid(row,col)) {
			file = colToFile(col);
			rank = rowToRank(row);
		}
	}
	
	
	public void setFile(char file) throws InvalidPositionException {
		if(isValid(file,rank))
			this.file = file;
		else
			throw new InvalidPositionException();
	}
	
	public void setRank(int rank) throws InvalidPositionException {
		if(isValid(file,rank))
			this.rank = rank;
		else
			throw new InvalidPositionException();
	}
	
	
	/* Update Methods */
	public void update(Position p) {
		file = p.getFile();
		rank = p.getRank();
	}
	
	public void update(char pfile, char prank) throws InvalidPositionException {
		if( isValid(pfile,prank)) {
			file = pfile;
			rank = prank;
		}
		else throw new InvalidPositionException();
	}
	
	public void update(String pos) throws InvalidPositionException {
		if( isValid(pos)) {
			file = pos.charAt(0);
			rank = pos.charAt(1) - '0';
		}
		else throw new InvalidPositionException();
	}
	
	public void update(int row, int col) throws InvalidPositionException {
		if( isValid(row, col)) {
			file = colToFile(col);
			rank = rowToRank(row);
		}
		else throw new InvalidPositionException();
	}	
	
	
	/* Static Methods for validation */
	protected  static boolean isValid(char pfile, int prank) {
		return (pfile >= 'a' && pfile <= 'h') && (prank >= 1 && prank <= 8);
		
	}
	
	protected  static boolean isValid(String str) {
		if(str.length() == 2) {
			char pfile = str.charAt(0);
			int prank = str.charAt(1) - '0';
			return isValid(pfile, prank);
		}
		return false;
	}
	
	protected static boolean isValid(int x, int y) {
		return (x >= 0 && x < 8 && y >= 0 && y < 8);
	}
	
	
	/*Static Methods for Conversion*/
	public static char colToFile(int col) {
		return (char)('a' + col);
	}
	
	public static int rowToRank(int row) {
		return 8 - row;
	}
	
	public static int fileToCol(char pfile) {
		return pfile - 'a';
	}
	
	public static int rankToRow(int prank) {
		return 8 - prank;
	}

	/*Boolean method to check is a position is NULL*/
	public boolean isNull() {
		return (this == null);
	}
	/* Boolean method to check if two positions are equal*/
	
	public boolean equals(Object p) {
		return (file == ((Position) p).getFile() && rank == ((Position) p).getRank());
	}
	
	/* Boolean methods to check the color of the Position*/
	public boolean isWhite() {
		return ((getRow() + getCol()) % 2 == 0);
	}
	
	public boolean isBlack() {
		return !isWhite();
	}

	/* Boolean method to check the location */
	public boolean isOnLeftEdge() {
		return (file == 'a');
	}
	
	public boolean isOnRightEdge() {
		return (file == 'h');
	}
	
	public boolean isOnTopEdge() {
		return (rank == 8);
	}
	
	public boolean isOnBottomEdge() {
		return (rank == 1);
	}
	
	public boolean isTopLeftCorner() {
		return isOnTopEdge() && isOnLeftEdge();
	}
	
	public boolean isTopRightCorner() {
		return isOnTopEdge() && isOnRightEdge();
	}
	
	public boolean isBottomLeftCorner() {
		return isOnBottomEdge() && isOnLeftEdge();
	}
	
	public boolean isBottomRightCorner() {
		return isOnBottomEdge() && isOnRightEdge();
	}
	
	public boolean isOnEdge() {
		return (isOnLeftEdge() ||  isOnRightEdge() || isOnTopEdge() || isOnBottomEdge());
	}
	
	public boolean hasLeft() {
		return !isOnLeftEdge();
	}
	
	public boolean hasRight() {
		return !isOnRightEdge();
	}
	
	public boolean hasTop() {
		return !isOnTopEdge();
	}
	
	public boolean hasBottom() {
		return !isOnBottomEdge();
	}
	
	public boolean hasTopLeft() {
		return hasTop() && hasLeft();
	}
	
	public boolean hasTopRight() {
		return hasTop() && hasRight();
	}
	
	public boolean hasBottomLeft() {
		return hasBottom() && hasLeft();
	}
	
	public boolean hasBottomRight() {
		return hasBottom() && hasRight();
	}
	
	
	/* Accessor Methods */
	public char getFile() {
		return file;
	}

	public int getRank() {
		return rank;
	}
	
	public int getRow() {
		return rankToRow(rank);
	}
	
	public int getCol() {
		return fileToCol(file);
	}

	
	/* Methods to retrive nearby positions if exists*/
	
	public Position clone() {
		Position p = null;
		try {
			p = new Position(file, rank);
		} catch (InvalidPositionException e) { }
		return p;
	}
	
	
	public Position getLeft(int n) throws InvalidPositionException {
		return new Position((char)(file - n), rank);
	}
	
	public Position getLeft() throws InvalidPositionException {
		return getLeft(1);
	}
	
	public Position getRight(int n) throws InvalidPositionException {
		return new Position((char)(file + n), rank);
	}
	
	public Position getRight() throws InvalidPositionException {
		return getRight(1);
	}
	
	public Position getTop(int n) throws InvalidPositionException {
		//System.out.println(toString());
		return new Position(file, rank + n);
	}
	
	public Position getTop() throws InvalidPositionException {
		return getTop(1);
	}
	
	public Position getBottom(int n) throws InvalidPositionException {
		return new Position(file, rank - n);
	}
	
	public Position getBottom() throws InvalidPositionException {
		return getBottom(1);
	}
	
	public Position getTopLeft(int n) {
		return getTop(n).getLeft(n);
	}
	public Position getTopLeft() {
		return getTop(1).getLeft(1);
	}
	
	public Position getTopRight(int n) {
		return getTop(n).getRight(n);
	}
	public Position getTopRight() {
		return getTop(1).getRight(1);
	}
	
	public Position getBottomLeft(int n) {
		return getBottom(n).getLeft(n);
	}
	public Position getBottomLeft() {
		return getBottom(1).getLeft(1);
	}
	
	public Position getBottomRight(int n) {
		return getBottom(n).getRight(n);
	}
	public Position getBottomRight() {
		return getBottom(1).getRight(1);
	}
	
	
	public ArrayList<Position> getNeighbours() throws InvalidPositionException {
		ArrayList<Position> neighbours = new ArrayList<Position>(8);
		
		if(hasLeft())
			neighbours.add(getLeft());
		if(hasRight())
			neighbours.add(getRight());
		if(hasTop())
			neighbours.add(getTop());
		if(hasBottom())
			neighbours.add(getBottom());
		if(hasLeft() && hasTop())
			neighbours.add(getLeft().getTop());
		if(hasLeft() && hasBottom())
			neighbours.add(getLeft().getBottom());
		if(hasRight()  && hasTop())
			neighbours.add(getRight().getTop());
		if(hasRight() && hasBottom())
			neighbours.add(getRight().getBottom());
		
		return neighbours;
	}
	
	public ArrayList<Position> getAllLeft() {
		ArrayList<Position> left = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_position = null;
		for(; temp.hasLeft();) {
			new_position = temp.getLeft();
			left.add(new_position);
			temp = new_position;
		}
		return left;
	}
	
	public ArrayList<Position> getAllRight() {
		ArrayList<Position> right = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		for(; temp.hasRight();) {
			new_pos = temp.getRight();
			right.add(new_pos);
			temp = new_pos;
		}
		return right;
	}
	
	public ArrayList<Position> getWholeRank() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		list.addAll(getAllLeft());
		list.addAll(getAllRight());
		return list;
	}
	
	public ArrayList<Position> getAllTop() {
		ArrayList<Position> top = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		for(; temp.hasTop();) {
			new_pos = temp.getTop();
			top.add(new_pos);
			temp = new_pos;
		}
		return top;
	}
	
	public ArrayList<Position> getAllBottom() {
		ArrayList<Position> bottom = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		while(temp.hasBottom()) {
			new_pos = temp.getBottom();
			bottom.add(new_pos);
			temp = new_pos;
		}
		return bottom;
	}
	
	public ArrayList<Position> getTopRightDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		while(temp.hasTopRight()) {
			new_pos = temp.getTopRight();
			list.add(new_pos);
			temp = new_pos;
		}
		return list;
	}
	
	public ArrayList<Position> getBottomLeftDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		while(temp.hasBottomLeft()) {
			new_pos = temp.getBottomLeft();
			list.add(new_pos);
			temp = new_pos;
		}
		return list;
	}
	
	public ArrayList<Position> getTopLeftDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		while(temp.hasTopLeft()) {
			new_pos = temp.getTopLeft();
			list.add(new_pos);
			temp = new_pos;
		}
		return list;
	}
	
	public ArrayList<Position> getBottomRightDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		Position temp = clone();
		Position new_pos = null;
		while(temp.hasBottomRight()) {
			new_pos = temp.getBottomRight();
			list.add(new_pos);
			temp = new_pos;
		}
		return list;
	}
	
	public ArrayList<Position> getPrimDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		list.addAll(getTopLeftDiagnal());
		list.addAll(getBottomRightDiagnal());
		return list;
	}
	
	public ArrayList<Position> getSecDiagnal() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		list.addAll(getTopRightDiagnal());
		list.addAll(getBottomLeftDiagnal());
		return list;
	}
	
	public ArrayList<Position> getWholeFile() {
		ArrayList<Position> list = new ArrayList<Position>(8);
		list.addAll(getAllTop());
		list.addAll(getAllBottom());
		return list;
	}
	
	public String toString() {
		if(isNull())
			return "null";
		char[] str = new char[2];
		str[0] = file;
		str[1] = (char) ('0' + rank);
		return new String(str);
	}
	
	public Color color() {
		if(isWhite())
			return Color.WHITE;
		else
			return Color.BLACK;
	}
	

}
