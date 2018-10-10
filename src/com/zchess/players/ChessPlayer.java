package com.zchess.players;

import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Logger;

import com.zchess.gameplay.Chessboard;
import com.zchess.gameplay.Color;
import com.zchess.gameplay.Controller;
import com.zchess.movements.Move;
import com.zchess.movements.Position;
import com.zchess.pieces.Chessmen;
import com.zchess.pieces.Pawn;
import com.zchess.pieces.Queen;
import com.zchess.pieces.Rook;
import com.zchess.pieces.Bishop;
import com.zchess.pieces.King;
import com.zchess.pieces.Knight;

/**
 * 
 * @author Maharshi
 *
 */
public class ChessPlayer {
	
	protected Logger logger = Controller.logger;
	
	protected Color color_var;
	protected String name_var;
	protected Chessboard board;
	
	protected King king;
	protected Queen queen;
	protected Rook[] rooks = new Rook[N_MINOR_PIECES];
	protected Bishop[] bishops = new Bishop[N_MINOR_PIECES];
	protected Knight[] knights = new Knight[N_MINOR_PIECES];
	protected Pawn[] pawns = new Pawn[N_PAWNS];
	
	protected ArrayList<Chessmen> chessmenSet = new ArrayList<Chessmen>(32);
	
	protected Move lastMove;
	protected Chessmen lastMoved;
	protected Chessmen lastCaptured;
	protected ChessPlayer opponent;
	
	protected boolean kingMoved;
	protected boolean leftRookMoved;
	protected boolean rightRookMoved;
	protected boolean castled;
	protected boolean queenMoved;
	
	protected int castleMoveNo;
	protected int kingFirstMoveNo;
	protected int leftRookFirstMoveNo;
	protected int rightRookFirstMoveNo;
	protected int queenFirstMoveNo;
	
	protected Stack<Move> moveStack;
	protected Stack<Chessmen> captureStack;
	
	public ChessPlayer(String name, Color color, Chessboard board) {
		
		name_var = name;
		color_var = color;
		this.board = board;
		
		king = new King(color);
		chessmenSet.add(king);
		
		queen = new Queen(color);
		chessmenSet.add(queen);
		
		for(int i=0; i<N_MINOR_PIECES; i++) {
			rooks[i] = new Rook(color);
			chessmenSet.add(rooks[i]);
			
			bishops[i] = new Bishop(color);
			chessmenSet.add(bishops[i]);
			
			knights[i] = new Knight(color);
			chessmenSet.add(knights[i]);
		}
		
		for(int i=0; i<N_PAWNS; i++) {
			pawns[i] = new Pawn(color);
			chessmenSet.add(pawns[i]);
		}
		
		moveStack = new Stack<Move>();
		captureStack = new Stack<Chessmen>();
		setupPieces();
		logger.info("Player: " + toString() + " setup.");
	}
	
	public int defaultPawnRank() {
		return isWhite() ? W_PAWN_RANK : B_PAWN_RANK;
	}
	
	public Position defaultKingPosition() {
		return isWhite() ? W_KING_POSITION : B_KING_POSITION;
	}
	
	public Position defaultQueenPosition() {
		return isWhite() ? W_QUEEN_POSITION : B_QUEEN_POSITION;
	}
	
	public Position defaultLeftRookPosition() {
		return isWhite() ? W_ROOK_LEFT_POSITION : B_ROOK_LEFT_POSITION;
	}
	
	public Position defaultRightRookPosition() {
		return isWhite() ? W_ROOK_RIGHT_POSITION : B_ROOK_RIGHT_POSITION;
	}
	
	public Position defaultLeftBishopPosition() {
		return isWhite() ? W_BISHOP_LEFT_POSITION : B_BISHOP_LEFT_POSITION;
	}
	
	public Position defaultRightBishopPosition() {
		return isWhite() ? W_BISHOP_RIGHT_POSITION : B_BISHOP_RIGHT_POSITION;
	}
	
	public Position defaultLeftKnightPosition() {
		return isWhite() ? W_KNIGHT_LEFT_POSITION : B_KNIGHT_LEFT_POSITION;
	}
	
	public Position defaultRightKnightPosition() {
		return isWhite() ? W_KNIGHT_RIGHT_POSITION : B_KNIGHT_RIGHT_POSITION;
	}
	
	
	public void setupPieces() {
		for(int i=0; i<N_PAWNS; i++) {
			board.add(pawns[i], new Position(Position.colToFile(i),defaultPawnRank()));
		}
		board.add(rooks[0],   defaultLeftRookPosition());
		board.add(knights[0], defaultLeftKnightPosition());
		board.add(bishops[0], defaultLeftBishopPosition());
		board.add(queen,      defaultQueenPosition());
		board.add(king,       defaultKingPosition());
		board.add(bishops[1], defaultRightBishopPosition());
		board.add(knights[1], defaultRightKnightPosition());
		board.add(rooks[1],   defaultRightRookPosition());
	}
	
	//sets the Opponent player
	public void setOpponent(ChessPlayer player) {
		if(isOpponent(player))
			opponent = player;
	}
		
	public void setBoard(Chessboard p_board) {
		board = p_board;
	}
	
	
	public String name() {
		return name_var;
	}
	
	public Color getColor() {
		return color_var;
	}
		
	public ChessPlayer getOpponent() {
		return opponent;
	}
	
	public Chessboard getBoard() {
		return board;
	}
	
	
	public Chessmen king() {
		return king;
	}
	
	public Chessmen primaryQueen() {
		if(queen.isAlive()) 
			return queen;
		return null;
	}
	
	public ArrayList<Chessmen> allPieces() {
		return chessmenSet;
	}
	
	
	public ArrayList<Chessmen> alivePieces() {
		ArrayList<Chessmen> set = new ArrayList<Chessmen>(20);
		for(Chessmen piece : chessmenSet) {
			if(piece.isAlive())
				set.add(piece);
		}
		return set;
	}
	
	public ArrayList<Chessmen> pawns() {
		ArrayList<Chessmen> pieces = new ArrayList<Chessmen>();
		for(Chessmen c : chessmenSet) {
			if(c instanceof Pawn && c.isAlive()) pieces.add(c);
		}
		return pieces;
	}
	
	public ArrayList<Chessmen> knights() {
		ArrayList<Chessmen> pieces = new ArrayList<Chessmen>();
		for(Chessmen c : chessmenSet) {
			if(c instanceof Knight && c.isAlive()) pieces.add(c);
		}
		return pieces;
	}
	
	public ArrayList<Chessmen> bishops() {
		ArrayList<Chessmen> pieces = new ArrayList<Chessmen>();
		for(Chessmen c : chessmenSet) {
			if(c instanceof Bishop && c.isAlive()) pieces.add(c);
		}
		return pieces;
	}
	
	public ArrayList<Chessmen> rooks() {
		ArrayList<Chessmen> pieces = new ArrayList<Chessmen>();
		for(Chessmen c : chessmenSet) {
			if(c instanceof Rook && c.isAlive()) pieces.add(c);
		}
		return pieces;
	}
	
	public ArrayList<Chessmen> queens() {
		ArrayList<Chessmen> pieces = new ArrayList<Chessmen>();
		for(Chessmen c : chessmenSet) {
			if(c instanceof Queen && c.isAlive()) pieces.add(c);
		}
		return pieces;
	}

	
	public Chessmen lastMoved() {
		return lastMoved;
	}
	
	//returns the chessmen moved in given position
	public Chessmen pieceMoved(Move move) {
		return board.chessmenAt(move.initialPosition());
	}
	
	public Chessmen pieceAt(Position p) {
		return board.chessmenAt(p);
	}
	public String toString() {
		String str = name_var + " (" + color_var.toString() + ")";
		return str;
	}
	
	
	// Boolean Functions to know the color of player, piece and opponent
	public boolean isWhite() {
		return color_var == Color.WHITE;
	}
	
	public boolean isBlack() {
		return color_var == Color.BLACK;
	}
	
	public boolean isOpponent(ChessPlayer player) {
		return isWhite() == player.isBlack();
	}
	
	
	public boolean isMyOwnPiece(Chessmen piece) {
		return piece.color() == color_var;
	}
	
	public boolean isOpponentPiece(Chessmen piece) {
		return piece.color() != color_var;
	}
	
	public boolean isOccupied(Position p) {
		return board.isOccupied(p);
	}
	
	public boolean isVacant(Position p) {
		return board.isVacant(p);
	}
	
	//boolean function to check if a position is occupied and its occupied by Opponent's piece
	public boolean isOccupiedByOpponent(Position p) {
		return board.isOccupied(p) && isOpponentPiece(board.chessmenAt(p));
	}
	
	//boolean function to check if a position is occupied and its occupied by my piece
	public boolean isOccupiedByMe(Position p) {
		return board.isOccupied(p) && isMyOwnPiece(board.chessmenAt(p));
	}
	
	
	//returns a shortCastle move depending on the color of player
	public Move shortCastleMove() {
		return new Move(defaultKingPosition(),defaultKingPosition().getRight(2));
	}
	
	//returns a shortCastle move depending on the color of player
	public Move longCastleMove() {
		return new Move(defaultKingPosition(), defaultKingPosition().getLeft(2));
	}
	
	//checks if its valid to perform shortCastle move at this time
	public boolean isShortCastleValid() {
		if(!castled && !kingMoved && !rightRookMoved 
				&& rooks[1].isAlive()
				&& board.isVacant(king.position().getRight())
				&& board.isVacant(king.position().getRight(2)))
				return true;
		return false;
	}
		
	//checks if its valid to perform longCAstle move at this time
	public boolean isLongCastleValid() {
		if(!castled && !kingMoved && !leftRookMoved 
				&& rooks[0].isAlive()
				&& board.isVacant(king.position().getLeft())
				&& board.isVacant(king.position().getLeft(2))
				&& board.isVacant(king.position().getLeft(3)))
			return true;
		return false;
	}
		
	//checks if given move is a shortCastle move
	public boolean isShortCastle(Move move) {
		return move.equals(shortCastleMove());
	}
	
	//checks if given move is longCastle move
	public boolean isLongCastle(Move move) {
		return move.equals(longCastleMove());
	}

	//checks if move is technically possible on chessBoard i.e. initial position should be occupied by
	//ownPiece and final position should be vacant or occupied by Opponent
	public boolean isPossible(Move move) {
		Position init = move.initialPosition();
		Position fin = move.finalPosition();
		return (isOccupiedByMe(init) && !isOccupiedByMe(fin));
	}
		
		
	//Checking whether given possible move is PawnPromotion move
	public boolean isPawnPromotion(Move move) {
		boolean promotion = false;
		Chessmen piece = pieceMoved(move);
		if(piece instanceof Pawn) {
			
			 if((piece.isWhite() && (move.initialPosition().getRank() == B_PAWN_RANK))
					|| (piece.isBlack() && (move.initialPosition().getRank() == W_PAWN_RANK))) {
				 promotion = true;
				// System.out.println("Buggy PP: "+move.toString()+ board.chessmenAt(move.initialPosition()));
			 }
		}
		return promotion;
	}
	
	//All valid moves of individual pieces...
	//might not be legal..may result under check
	public ArrayList<Move> pawnMoves(Pawn pawn) {
		ArrayList<Move> moveSet = new ArrayList<Move>();
		Position current_p = pawn.position().clone();
		
		if(isWhite() && pawn.isWhite()) {
			
			if(board.isVacant(current_p.getTop())) {
				moveSet.add(new Move(current_p, current_p.getTop()));
				if(pawn.rank() == W_PAWN_RANK && board.isVacant(current_p.getTop(2)))
					moveSet.add(new Move(current_p, current_p.getTop(2)));
			}
			
			if(current_p.hasTopLeft() && isOccupiedByOpponent(current_p.getTopLeft())) {
				moveSet.add(new Move(current_p, current_p.getTopLeft()));
			}
			
			if(current_p.hasTopRight() && isOccupiedByOpponent(current_p.getTopRight())) {
				moveSet.add(new Move(current_p, current_p.getTopRight()));
			}
		}
		
		else if(isBlack() && pawn.isBlack()) {
			
			if(board.isVacant(current_p.getBottom())) {
				moveSet.add(new Move(current_p, current_p.getBottom()));
				if(pawn.rank() == B_PAWN_RANK && board.isVacant(current_p.getBottom(2)))
					moveSet.add(new Move(current_p, current_p.getBottom(2)));
			}
			
			if(current_p.hasBottomLeft() && isOccupiedByOpponent(current_p.getBottomLeft())) {
				moveSet.add(new Move(current_p, current_p.getBottomLeft()));
			}
			
			if(current_p.hasTopRight() && isOccupiedByOpponent(current_p.getBottomRight())) {
				moveSet.add(new Move(current_p, current_p.getBottomRight()));
			}
		}
		return moveSet;
	}
	
	public ArrayList<Move> knightMoves(Knight knight) {
		Position p = knight.position().clone();
		Position pnext;
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(isOpponentPiece(knight))
			return moves;
		
		if(p.hasTopLeft()) {
			pnext = p.getTopLeft();
			if(pnext.hasTop() && (board.isVacant(pnext.getTop()) || isOccupiedByOpponent(pnext.getTop()))) {
				moves.add(new Move(p, pnext.getTop()));
			}
			if(pnext.hasLeft() && (board.isVacant(pnext.getLeft()) || isOccupiedByOpponent(pnext.getLeft()))) {
				moves.add(new Move(p, pnext.getLeft()));
			}
		}
		if(p.hasTopRight()) {
			pnext = p.getTopRight();
			if(pnext.hasTop() && (board.isVacant(pnext.getTop()) || isOccupiedByOpponent(pnext.getTop()))) {
				moves.add(new Move(p, pnext.getTop()));
			}
			if(pnext.hasRight() && (board.isVacant(pnext.getRight()) || isOccupiedByOpponent(pnext.getRight()))) {
				moves.add(new Move(p, pnext.getRight()));
			}
		}
		if(p.hasBottomLeft()) {
			pnext = p.getBottomLeft();
			if(pnext.hasBottom() && (board.isVacant(pnext.getBottom()) || isOccupiedByOpponent(pnext.getBottom()))) {
				moves.add(new Move(p, pnext.getBottom()));
			}
			if(pnext.hasLeft() && (board.isVacant(pnext.getLeft()) || isOccupiedByOpponent(pnext.getLeft()))) {
				moves.add(new Move(p, pnext.getLeft()));
			}
		}
		if(p.hasBottomRight()) {
			pnext = p.getBottomRight();
			if(pnext.hasBottom() && (board.isVacant(pnext.getBottom()) || isOccupiedByOpponent(pnext.getBottom()))) {
				moves.add(new Move(p, pnext.getBottom()));
			}
			if(pnext.hasRight() && (board.isVacant(pnext.getRight()) || isOccupiedByOpponent(pnext.getRight()))) {
				moves.add(new Move(p, pnext.getRight()));
			}
		}
		return moves;
	}
	
	public ArrayList<Move> bishopMoves(Bishop bishop) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(isOpponentPiece(bishop))
			return moves;
		
		Position p = bishop.position();
		
		Position temp = p;
		while(temp.hasTopLeft() && board.isVacant(temp.getTopLeft())) {
			moves.add(new Move(p, temp.getTopLeft()));
			temp = temp.getTopLeft();
		}
		if(temp.hasTopLeft() && isOccupiedByOpponent(temp.getTopLeft()))
			moves.add(new Move(p, temp.getTopLeft()));
		
		temp = p;
		while(temp.hasTopRight() && board.isVacant(temp.getTopRight())) {
			moves.add(new Move(p, temp.getTopRight()));
			temp = temp.getTopRight();
		}
		if(temp.hasTopRight() && isOccupiedByOpponent(temp.getTopRight()))
			moves.add(new Move(p, temp.getTopRight()));
		
		temp = p;
		while(temp.hasBottomLeft() && board.isVacant(temp.getBottomLeft())) {
			moves.add(new Move(p, temp.getBottomLeft()));
			temp = temp.getBottomLeft();
		}
		if(temp.hasBottomLeft() && isOccupiedByOpponent(temp.getBottomLeft()))
			moves.add(new Move(p, temp.getBottomLeft()));
		
		temp = p;
		while(temp.hasBottomRight() && board.isVacant(temp.getBottomRight())) {
			moves.add(new Move(p, temp.getBottomRight()));
			temp = temp.getBottomRight();
		}
		if(temp.hasBottomRight() && isOccupiedByOpponent(temp.getBottomRight()))
			moves.add(new Move(p, temp.getBottomRight()));
		
		return moves;
	}
	
	public ArrayList<Move> rookMoves(Rook rook) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(isOpponentPiece(rook))
			return moves;
		
		Position p = rook.position();
		
		Position temp = p;
		while(temp.hasTop() && board.isVacant(temp.getTop())) {
			moves.add(new Move(p, temp.getTop()));
			temp = temp.getTop();
		}
		if(temp.hasTop() && isOccupiedByOpponent(temp.getTop()))
			moves.add(new Move(p, temp.getTop()));
		
		temp = p;
		while(temp.hasBottom() && board.isVacant(temp.getBottom())) {
			moves.add(new Move(p, temp.getBottom()));
			temp = temp.getBottom();
		}
		if(temp.hasBottom() && isOccupiedByOpponent(temp.getBottom()))
			moves.add(new Move(p, temp.getBottom()));
		
		temp = p;
		while(temp.hasLeft() && board.isVacant(temp.getLeft())) {
			moves.add(new Move(p, temp.getLeft()));
			temp = temp.getLeft();
		}
		if(temp.hasLeft() && isOccupiedByOpponent(temp.getLeft()))
			moves.add(new Move(p, temp.getLeft()));
		
		temp = p;
		while(temp.hasRight() && board.isVacant(temp.getRight())) {
			moves.add(new Move(p, temp.getRight()));
			temp = temp.getRight();
		}
		if(temp.hasRight() && isOccupiedByOpponent(temp.getRight()))
			moves.add(new Move(p, temp.getRight()));
		
		return moves;
	}
	
	public ArrayList<Move> queenMoves(Queen queen) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Chessmen piece = new Rook(color_var);
		piece.assignPosition(queen.position());
		moves.addAll(rookMoves((Rook) piece));
		
		piece = new Bishop(color_var);
		piece.assignPosition(queen.position());
		moves.addAll(bishopMoves((Bishop) piece));
		return moves;
	}
	
	public ArrayList<Move> kingMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();
		Position p1 = king.position();
		ArrayList<Position> plist = king.position().getNeighbours();
		for(Position p2  : plist) {
			if(!isOccupiedByMe(p2)) {
				moves.add(new Move(p1,p2));
			}
		}
		//including Castle moves only if its Valid Legal to avoid double checking
		if(isShortCastleValid()) 
			moves.add(shortCastleMove());
		if(isLongCastleValid()) 
			moves.add(longCastleMove());
		return moves;
	}
	
	public ArrayList<Move> moves(Chessmen piece) {
		if(isOpponentPiece(piece))
			return new ArrayList<Move>();
		
		else if(piece instanceof King)
			return kingMoves();
		
		else if(piece instanceof Queen)
			return queenMoves((Queen) piece);
		
		else if(piece instanceof Rook)
			return rookMoves((Rook) piece);
		
		else if(piece instanceof Bishop)
			return bishopMoves((Bishop) piece);
		
		else if(piece instanceof Knight)
			return knightMoves((Knight) piece);
		
		else
			return pawnMoves((Pawn) piece);
	}
	
	//All Valid Moves but might not be Legal
	public ArrayList<Move> allMoves() {
		ArrayList<Move> moveset = new ArrayList<Move>();
		for(Chessmen piece : alivePieces()) {
			moveset.addAll(moves(piece));
		}
		//System.out.println(moveset.toString());
		return moveset;
	}
		
	
	//assuming its possible it checks if given move violates the individual piece moves rule.
	public boolean isValid(Move move) {
		Chessmen piece = pieceMoved(move);
		return moves(piece).contains(move);
	}
	
	
	//Assuming its possible and valid bt only for general moves excluding castle and pawnPromotion
	public void makeMove(Position initial_p, Position final_p, String pname) {
			
		if(board.isVacant(final_p)) {
			board.move(initial_p, final_p);
			lastCaptured = null;
		}
		else {
			lastCaptured = board.chessmenAt(final_p);
			board.removeAt(final_p);
			board.move(initial_p, final_p);
		}
		lastMove = new Move(initial_p, final_p);
		lastMoved = board.chessmenAt(final_p);
	}
	
	public void makeMove(String ip, String fp) {
		makeMove(new Position(ip), new Position(fp), " ");
	}
	
	public void makeMove(Chessmen piece, Position p) {
		makeMove(board.positionOf(piece), p, " ");
	}
	
	public void makeMove(Chessmen piece, String pos) {
		makeMove(board.positionOf(piece), new Position(pos), " ");
	}
	
	public void makeMove(Move move) {
		makeMove(move.initialPosition(), move.finalPosition(), move.promoType());
	}
	
	//Undo the last Move made
	//only for general moves excluding Castle and Pawn Promotion
	protected void undoLastMove() {
		board.move(lastMove.finalPosition(), lastMove.initialPosition());
		if(lastCaptured != null)
			board.add(lastCaptured, lastMove.finalPosition());
	}
	
	
	//function to check if a position is under attack by some opponent's piece
	public boolean isUnderAttack(Position p) {
		for(Move move : opponent.allMoves()) {
			if(move.finalPosition().equals(p))
				return true;
		}
		return false;
	}
	
	//checks if the king is under attack
	public boolean isUnderCheck() {
		return isUnderAttack(king.position());
	}
	
	//checks if shortCastle is valid and Legal
	public boolean canShortCastle() {
		if(isShortCastleValid()
				&& !isUnderCheck()
				&& !isUnderAttack(king.position().getRight())
				&& !isUnderAttack(king.position().getRight(2)))
			return true;
		return false;
	}
	
	//Checks if longCastle is valid and Legal
	public boolean canLongCastle() {
		if(isLongCastleValid()
				&& !isUnderCheck()
				&& !isUnderAttack(king.position().getLeft())
				&& !isUnderAttack(king.position().getLeft(2)))
			return true;
		return false;
	}
	
	
	//Assuming Pawn Promotion is possible, valid and Legal
	//Assuming move is made, this function only handles the promotion
	public Chessmen promotePawn(Pawn pawn, String name) {
		Chessmen piece = null;
		Position p = pawn.position();
		boolean success = true;
		if(name.equals("N")) {
			piece = new Knight(color_var);
		}
		else if(name.equals("B")) {
			piece = new Bishop(color_var);
		}
		else if(name.equals("R")) {
			piece = new Rook(color_var);
		}
		else if(name.equals("Q")) {
			piece = new Queen(color_var);
		}
		else {
			success = false;
		}
		if(success) {
			chessmenSet.add(piece);
			board.removeAt(p);
			board.add(piece, p);
		}
		return piece;		
	}
	
	//assuming that given move is a possible and Valid pawnPromotion move, it checks if it results in check
	public boolean canPromotePawn(Move move) {
		boolean ret_val = true;
		makeMove(move);
		if(isUnderCheck()) {
			ret_val = false;
		}
		undoLastMove();
		return ret_val;
	}

	//assumes it is possible and valid Move and 
	//checks if making that move results in having a check or retaining check if under check
	public boolean resultsInCheck(Move move) {
		if(isPawnPromotion(move)) {
			return !canPromotePawn(move);
		}
		else if(isShortCastle(move)) {
			System.out.print("SC "+move);
			return !canShortCastle();
		}
		else if(isLongCastle(move)) {
			System.out.print("LC: "+move+" "+!canLongCastle()+"\n");
			return !canLongCastle();
		}
		else {
			boolean ret_val = false;
			makeMove(move);
			if(isUnderCheck()) {
				ret_val = true;
			}
			undoLastMove();
			return ret_val;
		}
	}
	
	//checks if player can perform that move at the very instance legally
	public boolean isLegal(Move move) {
		return (isPossible(move) && isValid(move) && !resultsInCheck(move) );
	}
	
	
	//assuming short castle is possible, valid and legal its performs the move
	public void shortCastle() { 
		board.move(defaultKingPosition(), defaultRightKnightPosition());
		board.move(defaultRightRookPosition(), defaultRightBishopPosition());
	}
	
	//assuming long castle is possible valid and legal it performs the move
	public void longCastle() {
		board.move(defaultKingPosition(), defaultLeftBishopPosition());
		board.move(defaultLeftRookPosition(), defaultQueenPosition());
	}
	
	//Assuming given move is pawnPromotion and is possible, valid and legal
	public void finalPawnPromotion(Move move) {
		makeMove(move);
		Pawn p = (Pawn) board.chessmenAt(move.finalPosition());
		promotePawn(p, move.promoType());
	}
	
	// isLegal must be performed brfore executing this method
	//Checks each case individually for the moves validity and legality
	public void finalMove(Move move) {
		
		int nstep = moveStack.size() + 1;
		logger.info("Chessplayer: "+toString() + " making " + nstep + "th move :" + move.toString());
		if(isPawnPromotion(move) && canPromotePawn(move)) {
			//System.out.println("Checking for pawn promotion");
			finalPawnPromotion(move);
			//king and rookMoves flag need not be handled
		}
		
		else if(isShortCastle(move)) {
			shortCastle();
			kingMoved = true;
			leftRookMoved = true;
			castled = true;
			leftRookFirstMoveNo = kingFirstMoveNo = moveStack.size() + 1;
			castleMoveNo = moveStack.size() + 1;
			logger.info("Chessplayer: "+toString() + "shortCastled on " + castleMoveNo + "th move.");
			
		}
		
		else if(isLongCastle(move)) {
			longCastle();
			kingMoved = true;
			rightRookMoved = true;
			castled = true;
			rightRookFirstMoveNo = kingFirstMoveNo = moveStack.size() + 1;
			castleMoveNo = moveStack.size() + 1;
			logger.info("Chessplayer: "+toString() + "longCastled on " + castleMoveNo + "th move.");
		}
		
		else  {
			Chessmen piece = pieceMoved(move);
			makeMove(move);
			if(piece == king && !kingMoved) {
				kingMoved = true;
				kingFirstMoveNo = moveStack.size() + 1;
				logger.info("Chessplayer: "+toString() + "set kingMoved flag on "+ nstep+"th move");
			}
			else if(piece == rooks[0] && !leftRookMoved) {
				leftRookMoved = true;
				leftRookFirstMoveNo = moveStack.size() + 1;
				logger.info("Chessplayer: "+toString() + "set leftRookMoved flag on "+ nstep+"th move");
			}
			else if(piece == rooks[1] && !rightRookMoved) {
				rightRookMoved = true;
				rightRookFirstMoveNo = moveStack.size() + 1;
				logger.info("Chessplayer: "+toString() + "set rightRookMoved flag on "+ nstep+"th move");
			}
			else if(piece == queen && !queenMoved) {
				queenMoved = true;
				queenFirstMoveNo =  moveStack.size() + 1;
				logger.info("Chessplayer: "+toString() + "set queenMoved flag on "+ nstep+"th move");
			}
		}
		moveStack.push(move);
		captureStack.push(lastCaptured);
	}	
	
	public void finalTakeBack() {
		int size = moveStack.size();
		Move prevMove = moveStack.pop();
		Chessmen cPiece = captureStack.pop();
		logger.info("Chessplayer: "+toString() + " taking back " + size + "th move :" + prevMove.toString());
		if(prevMove.promoType() != " ") {
			Pawn pawn = getDeadPawn();
			Chessmen promotedPiece = board.chessmenAt(prevMove.finalPosition());
			board.remove(promotedPiece);
			chessmenSet.remove(promotedPiece);
			board.add(pawn, prevMove.initialPosition());
			if(cPiece != null)
				board.add(cPiece, prevMove.finalPosition());
		}
		
		else if(isShortCastle(prevMove)) {
			makeMove(rooks[1],defaultRightRookPosition());
			makeMove(king(),defaultKingPosition());
			kingMoved = false;
			rightRookMoved = false;
			castled = false;
			logger.info("Chessplayer: "+toString() + "took back Short Castle ");
			
		}
		
		else if(isLongCastle(prevMove)) {
			makeMove(rooks[0],defaultLeftRookPosition());
			makeMove(king(),defaultKingPosition());
			kingMoved = false;
			leftRookMoved = false;
			castled = false;
			logger.info("Chessplayer: "+toString() + "took back Long Castle ");
		}
		
		else {
			
			Chessmen piece = board.chessmenAt(prevMove.finalPosition());
			board.move(prevMove.finalPosition(), prevMove.initialPosition());
			if(cPiece != null)
				board.add(cPiece, prevMove.finalPosition());
			
			if(piece == king && kingFirstMoveNo == size) {
				kingMoved = false;
				logger.info("Chessplayer: "+toString() + "reset kingMoved flag on "+ size+"th move");
			}
			else if(piece == rooks[0] && leftRookFirstMoveNo == size) {
				leftRookMoved = false;
				logger.info("Chessplayer: "+toString() + "reset leftRookMoved flag on "+ size+"th move");
			}
			else if(piece == rooks[1] && rightRookFirstMoveNo == size) {
				rightRookMoved = false;
				logger.info("Chessplayer: "+toString() + "reset rightRookMoved flag on "+ size+"th move");
			}
			else if(piece == queen && queenFirstMoveNo == size) {
				queenMoved = false;
				logger.info("Chessplayer: "+toString() + "reset queenMoved flag on "+ size+"th move");
			}
		}
		
	}
	
	//Only used after assuring that there exists atleast one dead pawn in chessmenSet
	public Pawn getDeadPawn() {
		for(Pawn pawn : pawns) {
			if(pawn.isDead())
				return pawn;
		}
		return null;
	}
	
	//All Legal Moves a player has for now
	//when this list becomes empty at a point, It results in a kind of Mate situation
	public ArrayList<Move> allLegalMoves() {
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		for(Move move : allMoves()) {
			//System.out.println(move.toString());
			if(!resultsInCheck(move))
				legalMoves.add(move);
		}
		return legalMoves;
	}
	
	//checks if himslf is Mate
	public boolean isMate() {
		return allLegalMoves().isEmpty();
	}
	
	//checks if himself is checkMate
	public boolean isCheckMate() {
		return isMate() && isUnderCheck();
	}
	
	//checks if himself is staleMate
	public boolean isStaleMate() {
		return isMate() && !isUnderCheck();
	}
		
	
	protected static final int N_MINOR_PIECES = 2;
	protected static final int N_PAWNS = 8;
	protected static final int N_PIECES = 16;
	
	protected static final int W_PAWN_RANK = 2;
	protected static final int W_PAWN_PROMOTE_RANK = 7;
	protected static final int W_PIECE_RANK = 1;
	protected static final int B_PAWN_RANK = 7;
	protected static final int B_PAWN_PROMOTE_RANK = 2;
	protected static final int B_PIECE_RANK = 8;
	
	protected static final Position W_ROOK_LEFT_POSITION 		= 	new Position("a1");
	protected static final Position W_KNIGHT_LEFT_POSITION 	= 	new Position("b1");
	protected static final Position W_BISHOP_LEFT_POSITION 	= 	new Position("c1");
	protected static final Position W_QUEEN_POSITION 			= 	new Position("d1");
	protected static final Position W_KING_POSITION 			= 	new Position("e1");
	protected static final Position W_BISHOP_RIGHT_POSITION 	= 	new Position("f1");
	protected static final Position W_KNIGHT_RIGHT_POSITION 	= 	new Position("g1");
	protected static final Position W_ROOK_RIGHT_POSITION 	= 	new Position("h1");
	
	protected static final Position B_ROOK_LEFT_POSITION 		= 	new Position("a8");
	protected static final Position B_KNIGHT_LEFT_POSITION 	= 	new Position("b8");
	protected static final Position B_BISHOP_LEFT_POSITION 	= 	new Position("c8");
	protected static final Position B_QUEEN_POSITION 			= 	new Position("d8");
	protected static final Position B_KING_POSITION 			= 	new Position("e8");
	protected static final Position B_BISHOP_RIGHT_POSITION 	= 	new Position("f8");
	protected static final Position B_KNIGHT_RIGHT_POSITION 	= 	new Position("g8");
	protected static final Position B_ROOK_RIGHT_POSITION 	= 	new Position("h8");
}