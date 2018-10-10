package com.zchess.gameplay;

import java.util.ArrayList;

import com.zchess.movements.OccupiedPositionException;
import com.zchess.movements.Position;
import com.zchess.movements.VacantPositionException;
import com.zchess.pieces.Chessmen;
import com.zchess.movements.*;
import com.zchess.pieces.*;

public class Chessboard {

    private Chessmen[][] board;
    private int n_white_pieces;
    private int n_black_pieces;

    public Chessboard() {
        board = new Chessmen[8][8];
        n_white_pieces = 0;
        n_black_pieces = 0;
    }

    public void reset() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                if (isOccupied(p)) removeAt(p);
            }
        }
        n_white_pieces = 0;
        n_black_pieces = 0;
    }

    public void assign(Chessmen ch_men, Position p) {
        board[p.getRow()][p.getCol()] = ch_men;
        if (ch_men != null)
            ch_men.assignPosition(p);
    }

    public void add(Chessmen ch_men, Position p) {
        if (isOccupied(p))
            throw new OccupiedPositionException();
        else {
            assign(ch_men, p);
            if (ch_men.isBlack()) n_black_pieces++;
            else n_white_pieces++;
        }
    }

    public void move(Position p1, Position p2) {
        if (isOccupied(p2))
            throw new OccupiedPositionException();
        else if (isVacant(p1))
            throw new VacantPositionException();
        else {
            assign(chessmenAt(p1), p2);
            assign(null, p1);
        }
    }

    public boolean isVacant(Position p) {
        return chessmenAt(p) == null;
    }

    public boolean isOccupied(Position p) {
        return !isVacant(p);
    }

    public Chessmen chessmenAt(Position p) {
        Chessmen ch = board[p.getRow()][p.getCol()];
        return ch;
    }

    public Chessmen removeAt(Position p) {
        if (isVacant(p))
            throw new VacantPositionException();
        else {
            Chessmen ret_piece = chessmenAt(p);
            ret_piece.die();
            if (ret_piece.isBlack()) n_black_pieces--;
            else n_white_pieces--;
            assign(null, p);
            return ret_piece;
        }
    }

    public void remove(Chessmen piece) {
        removeAt(piece.position());
    }

    public ArrayList<Chessmen> whitePieces() {
        ArrayList<Chessmen> set = new ArrayList<Chessmen>(16);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                if (isOccupied(p) && chessmenAt(p).isWhite())
                    set.add(chessmenAt(p));
            }
        }
        return set;
    }

    public ArrayList<Chessmen> blackPieces() {
        ArrayList<Chessmen> set = new ArrayList<Chessmen>(16);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                if (isOccupied(p) && chessmenAt(p).isBlack())
                    set.add(chessmenAt(p));
            }
        }
        return set;
    }

    public ArrayList<Chessmen> allPieces() {
        ArrayList<Chessmen> set = whitePieces();
        set.addAll(blackPieces());
        return set;
    }

    public Position positionOf(Chessmen piece) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                if (isOccupied(p) && chessmenAt(p) == piece)
                    return p;
            }
        }
        return null;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Chessmen piece = chessmenAt(new Position(i, j));
                if (piece == null)
                    str += "|     " + new Position(i, j).toString() + " ";
                else
                    str += "| " + piece.toString() + " ";
            }
            str += "|\n";
        }
        return str;
    }

    public int nWhite() {
        return n_white_pieces;
    }

    public int nBlack() {
        return n_black_pieces;
    }

    public int nPieces() {
        return nWhite() + nBlack();
    }
}
