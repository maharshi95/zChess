package com.zchess.test;

import java.util.Scanner;

import com.zchess.gameplay.Chessboard;
import com.zchess.gameplay.Color;
import com.zchess.movements.InvalidPositionException;
import com.zchess.movements.Move;
import com.zchess.players.ZAgent;

public class Test {

	private static Scanner in;

	/**
	 * @param args
	 * @throws InvalidPositionException 
	 */
	
	public static void main(String[] args) throws InvalidPositionException {
		Chessboard board = new Chessboard();
		ZAgent mars = new ZAgent("Mars", Color.WHITE, board);
		ZAgent nike = new ZAgent("Nike", Color.BLACK, board);
		mars.setOpponent(nike);
		nike.setOpponent(mars);
		in = new Scanner(System.in);
		//System.out.println("\n"+board.toString());
		System.out.println(mars.pieceScore(nike.pawns().get(4)) + "\n");
		System.out.println(mars.pieceScore(nike.knights().get(1))+ "\n");
		nike.finalMove(new Move("e7","e5"));
		System.out.println(mars.pieceScore(nike.pawns().get(4))+ "\n");
		System.out.println(mars.pieceScore(nike.knights().get(1))+ "\n");
		while(!mars.isCheckMate() && !nike.isCheckMate()) {
			String str;
			String name = "";
			System.out.print("\nMars: ");
			str = in.nextLine();
			Move move = new Move(str.substring(0,2), str.substring(3));
			System.out.println(mars.isPossible(move)+ " "+mars.isValid(move) + " " + mars.isLegal(move));
			
			while(!mars.isLegal(move)) {
				System.out.println("Illegal Move!!");
				System.out.print("\nMars: ");
				str = in.nextLine();
				move = new Move(str.substring(0,2), str.substring(3));
			}
			if(mars.isPawnPromotion(move)) {
				System.out.print("What do u want to promote to : ");
				name = in.nextLine();
			}
			move.setPromotion(name);
			mars.finalMove(move);
			name = "";
			
			System.out.flush();
			System.out.println(move.toString());
			System.out.println("\n"+board.toString());
			
			if(!nike.isCheckMate()) {
				System.out.print("\nNike: ");
				str = in.nextLine();
				move = new Move(str.substring(0,2), str.substring(3));
				System.out.println("::"+mars.resultsInCheck(move));
				while(!nike.isLegal(move)) {
					System.out.println("Illegal Move!!");
					System.out.print("\nNike: ");
					str = in.nextLine();
					move = new Move(str.substring(0,2), str.substring(3));
				}
				if(nike.isPawnPromotion(move)) {
					System.out.print("What do u want to promote to : ");
					name = in.nextLine();
				}
				move.setPromotion(name);
				nike.finalMove(move);
				name = "";
				System.out.flush();
				System.out.println(move.toString());
				System.out.println("\n"+board.toString());
			}
		}
		System.out.println("GAME OVER");
		if(mars.isCheckMate())
			System.out.println("MARS is checkmate");
		else
			System.out.println("NIKE is checkmate");
		
	}
	
	public void print(String str) {
		System.out.println(str);
	}
}
