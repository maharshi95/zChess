package com.zchess.players;

import java.util.ArrayList;
import java.util.Random;

import com.zchess.gameplay.Chessboard;
import com.zchess.gameplay.Color;
import com.zchess.movements.Move;
import com.zchess.movements.Position;
import com.zchess.pieces.Chessmen;
import com.zchess.pieces.Pawn;
import com.zchess.pieces.Queen;
import com.zchess.pieces.Rook;
import com.zchess.pieces.Bishop;
import com.zchess.pieces.King;
import com.zchess.pieces.Knight;

public class ZAgent extends ChessPlayer {

	private Random Rgen = new Random();
	private boolean human;
	private int depth = 1;
	private Move checkmateMove;

	public ZAgent(String name, Color color, Chessboard board) {
		super(name, color, board);
		human = true;
	}
	
	public void giveIntelligence() {
		human = false;
	}

	public ZAgent opponent() {
		return (ZAgent) opponent;
	}

	public boolean isHuman() {
		return human;
	}
	
	public boolean isEngine() {
		return !human;
	}
	
	public ZAgent whiteP() {
		if(isWhite())
			return this;
		else
			return opponent();
	}
	
	public ZAgent blackP() {
		if(isBlack())
			return this;
		else
			return opponent();
	}
	
	
	
	public ArrayList<Move> pawnRange(Pawn pawn) {
		ArrayList<Move> moveSet = new ArrayList<Move>();
		Position current_p = pawn.position();

		if (pawn.isWhite()) {

			if (current_p.hasTopLeft() && isOccupied(current_p.getTopLeft())) {
				moveSet.add(new Move(current_p, current_p.getTopLeft()));
			}

			if (current_p.hasTopRight() && isOccupied(current_p.getTopRight())) {
				moveSet.add(new Move(current_p, current_p.getTopRight()));
			}
		}

		else if (pawn.isBlack()) {

			if (current_p.hasBottomLeft()
					&& isOccupiedByOpponent(current_p.getBottomLeft())) {
				moveSet.add(new Move(current_p, current_p.getBottomLeft()));
			}

			if (current_p.hasTopRight()
					&& isOccupiedByOpponent(current_p.getBottomRight())) {
				moveSet.add(new Move(current_p, current_p.getBottomRight()));
			}
		}
		return moveSet;
	}

	public ArrayList<Move> knightRange(Knight knight) {

		Position p = knight.position();
		Position pnext;
		ArrayList<Move> moves = new ArrayList<Move>();

		if (p.hasTopLeft()) {
			pnext = p.getTopLeft();
			if (pnext.hasTop()) {
				moves.add(new Move(p, pnext.getTop()));
			}
			if (pnext.hasLeft()) {
				moves.add(new Move(p, pnext.getLeft()));
			}
		}
		if (p.hasTopRight()) {
			pnext = p.getTopRight();
			if (pnext.hasTop()) {
				moves.add(new Move(p, pnext.getTop()));
			}
			if (pnext.hasRight()) {
				moves.add(new Move(p, pnext.getRight()));
			}
		}
		if (p.hasBottomLeft()) {
			pnext = p.getBottomLeft();
			if (pnext.hasBottom()) {
				moves.add(new Move(p, pnext.getBottom()));
			}
			if (pnext.hasLeft()) {
				moves.add(new Move(p, pnext.getLeft()));
			}
		}
		if (p.hasBottomRight()) {
			pnext = p.getBottomRight();
			if (pnext.hasBottom()) {
				moves.add(new Move(p, pnext.getBottom()));
			}
			if (pnext.hasRight()) {
				moves.add(new Move(p, pnext.getRight()));
			}
		}
		return moves;
	}

	public ArrayList<Move> bishopRange(Bishop bishop) {
		ArrayList<Move> moves = new ArrayList<Move>();

		Position p = bishop.position();

		Position temp = p;
		while (temp.hasTopLeft() && board.isVacant(temp.getTopLeft())) {
			moves.add(new Move(p, temp.getTopLeft()));
			temp = temp.getTopLeft();
		}
		if (temp.hasTopLeft())
			moves.add(new Move(p, temp.getTopLeft()));

		temp = p;
		while (temp.hasTopRight() && board.isVacant(temp.getTopRight())) {
			moves.add(new Move(p, temp.getTopRight()));
			temp = temp.getTopRight();
		}
		if (temp.hasTopRight())
			moves.add(new Move(p, temp.getTopRight()));

		temp = p;
		while (temp.hasBottomLeft() && board.isVacant(temp.getBottomLeft())) {
			moves.add(new Move(p, temp.getBottomLeft()));
			temp = temp.getBottomLeft();
		}
		if (temp.hasBottomLeft())
			moves.add(new Move(p, temp.getBottomLeft()));

		temp = p;
		while (temp.hasBottomRight() && board.isVacant(temp.getBottomRight())) {
			moves.add(new Move(p, temp.getBottomRight()));
			temp = temp.getBottomRight();
		}
		if (temp.hasBottomRight())
			moves.add(new Move(p, temp.getBottomRight()));

		return moves;
	}

	public ArrayList<Move> rookRange(Rook rook) {
		ArrayList<Move> moves = new ArrayList<Move>();

		Position p = rook.position();

		Position temp = p;
		while (temp.hasTop() && board.isVacant(temp.getTop())) {
			moves.add(new Move(p, temp.getTop()));
			temp = temp.getTop();
		}
		if (temp.hasTop())
			moves.add(new Move(p, temp.getTop()));

		temp = p;
		while (temp.hasBottom() && board.isVacant(temp.getBottom())) {
			moves.add(new Move(p, temp.getBottom()));
			temp = temp.getBottom();
		}
		if (temp.hasBottom())
			moves.add(new Move(p, temp.getBottom()));

		temp = p;
		while (temp.hasLeft() && board.isVacant(temp.getLeft())) {
			moves.add(new Move(p, temp.getLeft()));
			temp = temp.getLeft();
		}
		if (temp.hasLeft())
			moves.add(new Move(p, temp.getLeft()));

		temp = p;
		while (temp.hasRight() && board.isVacant(temp.getRight())) {
			moves.add(new Move(p, temp.getRight()));
			temp = temp.getRight();
		}
		if (temp.hasRight())
			moves.add(new Move(p, temp.getRight()));

		return moves;
	}

	public ArrayList<Move> queenRange(Queen queen) {
		ArrayList<Move> moves = new ArrayList<Move>();

		Chessmen piece = new Rook(color_var);
		piece.assignPosition(queen.position());
		moves.addAll(rookRange((Rook) piece));

		piece = new Bishop(color_var);
		piece.assignPosition(queen.position());
		moves.addAll(bishopRange((Bishop) piece));
		return moves;
	}

	public ArrayList<Move> kingRange() {
		ArrayList<Move> moves = new ArrayList<Move>();
		Position p1 = king.position();
		ArrayList<Position> plist = king.position().getNeighbours();
		return moves;
	}

	public ArrayList<Move> pieceRange(Chessmen piece) {

		if (piece instanceof King)
			return kingRange();

		else if (piece instanceof Queen)
			return queenRange((Queen) piece);

		else if (piece instanceof Rook)
			return rookRange((Rook) piece);

		else if (piece instanceof Bishop)
			return bishopRange((Bishop) piece);

		else if (piece instanceof Knight)
			return knightRange((Knight) piece);

		else
			return pawnRange((Pawn) piece);
	}

	// to be implemented
	public boolean canCheckmate() {
		
		for(Move move : allLegalMoves()) {
			finalMove(move);
			if(opponent().isCheckMate()) {
				checkmateMove = move;
				return true;
			}
			finalTakeBack();
		}
		return false;
	}

	public boolean isStartGame() {
		return board.nPieces() > 25;
	}
	
	public boolean isEndGame() {
		return board.nPieces() < 12;
	}
	
	public boolean isMiddleGame() {
		return !isEndGame() && !isStartGame();
	}
	
	
	
	//should only be used immediately after canCheckmate() returns true
	private Move checkmateMove() {
		return checkmateMove;
	}

	private static int pieceValue(Chessmen piece) {
		int value;
		if (piece instanceof King)
			value = king_value;

		else if (piece instanceof Queen)
			value = queen_value;

		else if (piece instanceof Rook)
			value = rook_value;

		else if (piece instanceof Bishop)
			value = bishop_value;

		else if (piece instanceof Knight)
			value = knight_value;

		else
			value = pawn_value;
		
		if(piece.isBlack()) value = -value;
		return value;
	}

	private static int actingValue(Chessmen piece) {
		int value;
		
		if (piece instanceof King)
			value = king_action_value;

		else if (piece instanceof Queen)
			value = queen_action_value;

		else if (piece instanceof Rook)
			value = rook_action_value;

		else if (piece instanceof Bishop)
			value = bishop_action_value;

		else if (piece instanceof Knight)
			value = knight_action_value;

		else
			value = pawn_action_value;
		
		//if(piece.isBlack()) value = -value;
		return value;
	}
	
	//minor pieces have more defenced value
	private static int actedValue(Chessmen piece) {
		return MAX_ACTION - MIN_ACTION + actingValue(piece);
	}
	
	
	public int mobilityOf(Chessmen piece) {

		int mob;
		
		if (piece instanceof King)
			mob = kingMoves().size();

		else if (piece instanceof Queen)
			mob = queenMoves((Queen) piece).size();

		else if (piece instanceof Rook)
			mob = rookMoves((Rook) piece).size();

		else if (piece instanceof Bishop)
			mob = bishopMoves((Bishop) piece).size();

		else if (piece instanceof Knight)
			mob = knightMoves((Knight) piece).size();

		else
			mob = pawnMoves((Pawn) piece).size();
		
		if(piece.isBlack()) mob = -mob;
		return mob;
	}

	public int positionValueof(Chessmen piece) {
		
		int value = 0;
		int index = Index(piece.position());
		boolean white = piece.isWhite();
		//System.out.println("index : "+ index);
		if(piece instanceof Pawn) {
			value = white ? white_pawn_table[index] : black_pawn_table[index] ;
		}
		else if(piece instanceof Knight) {
			value = white ? white_knight_table[index] : black_knight_table[index] ;
		}
		else if(piece instanceof Bishop) {
			value = white ? white_bishop_table[index] : black_bishop_table[index] ;
		}
		else if(piece instanceof Rook) {
			value = white ? white_rook_table[index] : black_rook_table[index] ;
		}
		else if(piece instanceof Queen) {
			value = white ? white_queen_table[index] : black_queen_table[index] ;
		}
		else if(piece instanceof King && isEndGame()) {
			value = white ? white_king_table_endGame[index] : black_king_table_endGame[index] ;
		}
		else {
			value = white ? white_king_table[index] : black_king_table[index] ;
		}
		if(piece.isBlack()) value = -value;
		return value;
	}
	
	/**
	 * Method calculates the numerical value of its attacking characteristics on opponents pieces
	 * @param piece : piece whose attack is to be calculated
	 * @return attack value, +ve for white pieces and -ve for black pieces
	 */
	public int attack(Chessmen piece) {
		int power = 0;
		ArrayList<Move> range = pieceRange(piece);
		for(Move move : range) {
			Position p = move.finalPosition();
			Chessmen opp_piece = board.chessmenAt(p);
			if(board.isOccupied(p) && piece.isEnemy(opp_piece)) {
				power += (actingValue(piece) + actedValue(opp_piece))*mFactor;
			}
		}
		if(piece.isBlack())
			power = -power;
		return power;
	}
	
	/**
	 * Method calculates the numerical value of its defensing characteristics on own pieces
	 * @param piece : piece whose defense is to be calculated
	 * @return defence value, +ve for white pieces and -ve for black pieces
	 */
	public int defence(Chessmen piece) {
		int power = 0;
		ArrayList<Move> range = pieceRange(piece);
		for(Move move : range) {
			Position p = move.finalPosition();
			Chessmen own_piece = board.chessmenAt(p);
			if(board.isOccupied(p) && piece.isAlly(own_piece)) {
				power += (actingValue(piece) + actedValue(own_piece))*mFactor/2;
			}
		}
		if(piece.isBlack())
			power = -power;
		return power;
	}
	
	/**
	 * Method calculates the numerical value of its weakness, opponents pieces ability to attack on this piece
	 * @param piece : piece whose weakness is to be calculated
	 * @return weakness value, -ve for white pieces and +ve for black pieces
	 */
	public int weakness(Chessmen piece) {
		int power = 0;
		for(Chessmen opp_piece : board.allPieces()) {
			if(opp_piece.isEnemy(piece)) {
				
				ArrayList<Move> range = pieceRange(opp_piece);
				for(Move move : range) {
					Position p = move.finalPosition();
					if(p.equals(piece.position())) {
						power -= (actingValue(opp_piece) + actedValue(piece))*mFactor;
					}
				}
			}
		}
		if(piece.isBlack()) power = -power;
		return power;
	}
	
	/**
	 * Method calculates the numerical value of its strength, own pieces ability to defense this piece
	 * @param piece : piece whose strength is to be calculated
	 * @return weakness value, +ve for white pieces and -ve for black pieces
	 */
	public int strength(Chessmen piece) {
		int power = 0;
		for(Chessmen own_piece : board.allPieces()) {
			if(own_piece.isAlly(piece)) {
				
				ArrayList<Move> range = pieceRange(own_piece);
				for(Move move : range) {
					Position p = move.finalPosition();
					if(p.equals(piece.position())) {
						power += (actingValue(own_piece) + actedValue(piece))*mFactor;
					}
				}
			}
		}
		if(piece.isBlack()) power = -power;
		return power;
	}
	
	//to be implemented
	public int pieceScore(Chessmen piece) {
		
		int score = 0;
		int pieceValue = pieceValue(piece); 
		int mobility = mobilityOf(piece)*3;
		int position = positionValueof(piece);
		int attack = attack(piece);
		int defence = defence(piece);
		
		score +=  pieceValue;
		score += mobility;
		score += position;
		score += attack;
		score += defence;
		
		int survival = strength(piece) - weakness(piece);
//		System.out.println(piece.toString());
//		System.out.println("PieceValue : " + pieceValue);
//		System.out.println("Mobility : " + mobility);
//		System.out.println("PositionValue : " + position);
//		System.out.println("Attack : " + attack);
//		System.out.println("Defence : " + defence);
//		System.out.println("Survival : " + survival);
//		System.out.println("Score : " + score);
//		System.out.println();
		
//		if(piece.isWhite() && survival > 0)
//			score += 40;
//		else if(piece.isBlack() && survival < 0)
//			score += 40;
//	
		if(piece.isWhite() && survival < 0)
			score -= 200;
		else if(piece.isBlack() && survival > 0)
			score += 200;
		
		if(piece instanceof Pawn) {
			int rank = piece.position().getRank();
			if(piece.isWhite() && (rank == 5 || rank == 6)) 
				score += 80;
			else if(piece.isBlack() && (rank == 2 || rank == 3))
				score -= 80;
		}
		
		if(piece instanceof Knight) {
			if(piece.isWhite()) {
				if(isEndGame())
					score -= 10;
				else if(isMiddleGame()) {
					score += 10;
				}
			}
			else {
				if(isEndGame())
					score += 10;
				else if(isMiddleGame()) {
					score -= 10;
				}
			}
		}
		
		if(piece instanceof Bishop) {
			if(piece.isWhite()) {
				if(isEndGame())
					score += 10;
			}
			else {
				if(isEndGame())
					score -= 10;
			}
		}
		
		//System.out.println("Final score of piece: "+ score);
		return score;
	}
	
	// to be implemented
	public int boardScore() {
		int score = 0;
		
		if(whiteP().isStaleMate() || blackP().isStaleMate()) {
			score = 0;
		}
		
		else if(whiteP().isCheckMate()) {
			score = MIN_SCORE;
		}
		
		else if (blackP().isCheckMate()) {
			score = MAX_SCORE;
		}
		
		else {
			
			for(Chessmen piece : board.allPieces()) {
				score += pieceScore(piece);
			}
			//System.out.println("Iteration score: "+score);
			if(whiteP().bishops().size() == 2)
				score += 20;
			
			if(blackP().bishops().size() == 2)
				score -= 20;
			
			if(isStartGame()) {
				if(whiteP().queenMoved)
					score -= 10;
				if(blackP().queenMoved)
					score += 10;
			}
			
			if(!whiteP().castled && whiteP().kingMoved)
				score -= 40;
			
			if(!blackP().castled && blackP().kingMoved)
				score += 40;
			
			if(whiteP().kingMoves().size() < 4)
				score -= 30;
			
			if(blackP().kingMoves().size() < 4)
				score += 30;
			
			
			if(whiteP().isUnderCheck()) {
				score -= 75;
				if(isEndGame())
					score -= 10;
			}
			
			if(blackP().isUnderCheck()) {
				score += 75;
				if(isEndGame())
					score += 10;
			}
			
			if(whiteP().castled)
				score += 45;
			
			if(blackP().castled)
				score -= 45;
			
			if(whiteP().queen.isAlive() && whiteP().isUnderAttack(whiteP().queen.position()))
					score -= 500;
			if(blackP().queen.isAlive() && blackP().isUnderAttack(blackP().queen.position()))
				score += 500;
			
			
		}
		
	//	System.out.println("Iteration score: "+score);
		return score;

	}


	public Move whiteNextQuickBestMove() {
		System.out.println(whiteP().toString() + " zeroLevel" );
		Move best_move = null;
		int max_score = MIN_SCORE;
		for (Move move : whiteP().allLegalMoves()) {
			whiteP().finalMove(move);
			int score = boardScore();
			if (score >= max_score) {
				max_score = score;
				best_move = move;
			}
			whiteP().finalTakeBack();
		}
		return best_move;
	}
	public Move blackNextQuickBestMove() {
		System.out.println(blackP().toString() + " zeroLevel" );
		Move best_move = null;
		int min_score = MAX_SCORE;
		for (Move move : blackP().allLegalMoves()) {
			blackP().finalMove(move);
			int score = boardScore();
			if (score <= min_score) {
				min_score = score;
				best_move = move;
			}
			blackP().finalTakeBack();
		}
		return best_move;
	}

	
	public Move whiteBestMove(int level) {
		System.out.println(whiteP().toString() + " Level :" +level);
		if(level == 0)
			return whiteNextQuickBestMove();
		if (whiteP().canCheckmate()) 
			return whiteP().checkmateMove();
			
		Move best_move = null;
		int max_score = MIN_SCORE;
		for (Move move : whiteP().allLegalMoves()) {
			boolean black_moved = false;
			logger.info(whiteP().toString() + "thinking more at level " + level);
			
			if(whiteP().isPawnPromotion(move))
				move.setPromotion("Q");
			whiteP().finalMove(move);
			
			if(!blackP().isMate()) {
				Move opp_move = blackBestMove(level - 1);
				blackP().finalMove(opp_move);
				black_moved = true;				
			}
			int score = boardScore();
			if (score >= max_score) {
				max_score = score;
				best_move = move;
			}
			if(black_moved) blackP().finalTakeBack();
			whiteP().finalTakeBack();
		}
		return best_move;
	}
		
	public Move blackBestMove(int level) {
		System.out.println(blackP().toString() + " Level :" +level);
		if(level == 0)
			return blackNextQuickBestMove();
		if (blackP().canCheckmate()) 
			return blackP().checkmateMove();
			
		Move best_move = null;
		int min_score = MAX_SCORE;
		for (Move move : blackP().allLegalMoves()) {
			boolean white_moved = false;
			logger.info(blackP().toString() + "thinking more at level " + level);
			
			if(blackP().isPawnPromotion(move))
				move.setPromotion("Q");
			blackP().finalMove(move);
			
			if(!whiteP().isMate()) {
				Move opp_move = whiteBestMove(level - 1);
				whiteP().finalMove(opp_move);
				white_moved = true;
			}
			
			int score = boardScore();
			if (score <= min_score) {
				min_score = score;
				best_move = move;
			}
			if(white_moved) whiteP().finalTakeBack();
			blackP().finalTakeBack();
		}
		return best_move;
	}

	public Move getBestMove() {
		if(isWhite())
			return whiteBestMove(1);
		else
			return blackBestMove(1);
	}
	
	public Move getRandomMove() {
		ArrayList<Move> movelist = allLegalMoves();
		int index = Rgen.nextInt(movelist.size());
		return (movelist.get(index));
	}
	
	public static final int Index(Position p) {
		int row = p.getRow();
		int col = p.getCol();
		//System.out.println(p.toString() + row + " "+ col);
		return 8*row + col;
	}
	
	private static final Position position(int index) {
		return new Position(index / 8, index % 8);
	}
	
	private static final int MAX_SCORE = 65536;
	private static final int MIN_SCORE = -65536;
	
	private static final int MAX_ACTION = 6;
	private static final int MIN_ACTION = 1;
	
	
	private static final int pawn_value = 100;
	private static final int knight_value = 320;
	private static final int bishop_value = 325;
	private static final int rook_value = 500;
	private static final int queen_value = 970;
	private static final int king_value = 32767;

	private static final int pawn_action_value = 6;
	private static final int knight_action_value = 3;
	private static final int bishop_action_value = 3;
	private static final int rook_action_value = 2;
	private static final int queen_action_value = 1;
	private static final int king_action_value = 1;
	
	private static final int mFactor = 1;
	
	private static final int[] white_pawn_table = new int[]
			{
			  0,  0,  0,  0,  0,  0,  0,  0,
			 50, 50, 50, 50, 50, 50, 50, 50,
			 10, 10, 20, 30, 30, 20, 10, 10,
			  5,  5, 10, 27, 27, 10,  5,  5,
			  0,  0,  0, 25, 25,  0,  0,  0,
			  5, -5,-10, 10, 10,-10, -5,  5,
			  5, 10, 10,-65,-65, 10, 10,  5,
			  0,  0,  0,  0,  0,  0,  0,  0
			};
	private static final int[] white_knight_table = new int[]
			{
			 -50,-40,-30,-30,-30,-30,-40,-50,
			 -40,-20,  0,  0,  0,  0,-20,-40,
			 -30,  0, 10, 15, 15, 10,  0,-30,
			 -30,  5, 15, 20, 20, 15,  5,-30,
			 -30,  0, 15, 20, 20, 15,  0,-30,
			 -30,  5, 10, 15, 15, 10,  5,-30,
			 -40,-20,  0,  5,  5,  0,-20,-40,
			 -50,-40,-20,-30,-30,-20,-40,-50,
			};

	private static final int[] white_bishop_table = new int[]
			{
			 -20,-10,-10,-10,-10,-10,-10,-20,
			 -10,  0,  0,  0,  0,  0,  0,-10,
			 -10,  0,  5, 10, 10,  5,  0,-10,
			 -10,  5,  5, 10, 10,  5,  5,-10,
			 -10,  0, 10, 10, 10, 10,  0,-10,
			 -10, 10, 10, 10, 10, 10, 10,-10,
			 -10,  5,  0,  0,  0,  0,  5,-10,
			 -20,-10,-40,-10,-10,-40,-10,-20,
			};

	private static final int[] white_rook_table = new int[]
			{
			  20, 20, 20, 20, 20, 20, 20, 20,
			  20, 20, 20, 20, 20, 20, 20, 20,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			 -10,-10,  0,  0,  0,  0,-10,-10,
			   0,-20,-20, 10, 20, 10,-20,  0,
			};

	//to be edited
	private static final int[] white_queen_table = new int[]
			{
			 -20,-10,-10,-10,-10,-10,-10,-20,
			 -10,  0,  0,  0,  0,  0,  0,-10,
			 -10,  0,  5, 10, 10,  5,  0,-10,
			 -10,  5,  5, 10, 10,  5,  5,-10,
			 -10,  0, 10, 10, 10, 10,  0,-10,
			 -10, 10, 10, 10, 10, 10, 10,-10,
			 -10,  5,  0,  0,  0,  0,  5,-10,
			 -20,-10,-40,-10,-10,-40,-10,-20,
			};

	private static final int[] white_king_table = new int[]
			{
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -20, -30, -30, -40, -40, -30, -30, -20,
			  -10, -20, -20, -20, -20, -20, -20, -10, 
			   20,  20,   0,   0,   0,   0,  20,  20,
			   20,  30,  10,   0,   0,  10,  30,  20
			};

	private static final int[] white_king_table_endGame = new int[]
			{
			 -50,-40,-30,-20,-20,-30,-40,-50,
			 -30,-20,-10,  0,  0,-10,-20,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-30,  0,  0,  0,  0,-30,-30,
			 -50,-30,-30,-30,-30,-30,-30,-50
			};
	
	private static final int[] black_pawn_table = new int[]
			{
			  0,  0,  0,  0,  0,  0,  0,  0,
			  5, 10, 10,-65,-65, 10, 10,  5,
			  5, -5,-10, 10, 10,-10, -5,  5,
			  0,  0,  0, 25, 25,  0,  0,  0,
			  5,  5, 10, 27, 27, 10,  5,  5,
			  10, 10, 20, 30, 30, 20, 10, 10,
			  50, 50, 50, 50, 50, 50, 50, 50,
			  0,  0,  0,  0,  0,  0,  0,  0 
			};
	private static final int[] black_knight_table = new int[]
			{
			 -50,-40,-30,-30,-30,-30,-40,-50,
			 -40,-20,  0,  5,  5,  0,-20,-40,
			 -30,  5, 10, 15, 15, 10,  5,-30,
			 -30,  0, 15, 20, 20, 15,  0,-30,
			 -30,  5, 15, 20, 20, 15,  5,-30,
			 -30,  0, 10, 15, 15, 10,  0,-30,
			 -40,-20,  0,  0,  0,  0,-20,-40,
			 -50,-40,-20,-30,-30,-20,-40,-50
			};

	private static final int[] black_bishop_table = new int[]
			{
			 -20,-10,-40,-10,-10,-40,-10,-20,
			 -10,  0,  0,  0,  0,  0,  0,-10,
			 -10, 10, 10, 10, 10, 10, 10,-10,
			 -10,  0, 10, 10, 10, 10,  0,-10,
			 -10,  5,  5, 10, 10,  5,  5,-10,
			 -10,  0,  5, 10, 10,  5,  0,-10,
			 -10,  5,  0,  0,  0,  0,  5,-10,
			 -20,-10,-10,-10,-10,-10,-10,-20
			};

	private static final int[] black_rook_table = new int[]
			{
			   0,-20,-20, 10, 20, 10,-20,  0,
			 -10,-10,  0,  0,  0,  0,-10,-10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  10, 10, 10, 10, 10, 10, 10, 10,
			  20, 20, 20, 20, 20, 20, 20, 20,
			  20, 20, 20, 20, 20, 20, 20, 20
			  
			};

	//to be edited
	private static final int[] black_queen_table = new int[]
			{
			 -20,-10,-10,-10,-10,-10,-10,-20,
			 -10,  0,  0,  0,  0,  0,  0,-10,
			 -10,  0,  5, 10, 10,  5,  0,-10,
			 -10,  5,  5, 10, 10,  5,  5,-10,
			 -10,  0, 10, 10, 10, 10,  0,-10,
			 -10, 10, 10, 10, 10, 10, 10,-10,
			 -10,  5,  0,  0,  0,  0,  5,-10,
			 -20,-10,-40,-10,-10,-40,-10,-20
			};

	private static final int[] black_king_table = new int[]
			{
			   20,  30,  10,   0,   0,  10,  30,  20,
			   20,  20,   0,   0,   0,   0,  20,  20,
			   -10, -20, -20, -20, -20, -20, -20, -10, 
			   -20, -30, -30, -40, -40, -30, -30, -20,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			  -30, -40, -40, -50, -50, -40, -40, -30,
			   -30, -40, -40, -50, -50, -40, -40, -30,
			   -30, -40, -40, -50, -50, -40, -40, -30,
			};

	private static final int[] black_king_table_endGame = new int[]
			{
			 -50,-40,-30,-20,-20,-30,-40,-50,
			 -30,-20,-10,  0,  0,-10,-20,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 30, 40, 40, 30,-10,-30,
			 -30,-10, 20, 30, 30, 20,-10,-30,
			 -30,-30,  0,  0,  0,  0,-30,-30,
			 -50,-30,-30,-30,-30,-30,-30,-50
			};
}
