package com.zchess.pieces;

import java.io.*;
import java.util.*;

import com.zchess.gameplay.Color;
import com.zchess.movements.Position;
import com.zchess.movements.*;

public class Knight extends Chessmen {

    public Knight(Color color) {
        color_var = color;
        name_var = "N";
        p_var = null;
        try {
            if (color_var == Color.BLACK)
                img = getImage("Black/Knight.png");
            else
                img = getImage("White/Knight.png");
        } catch (IOException e) {
            throw new RuntimeException("Knight Icon Not Found");
        }
    }

    @Override
    public int value() {
        return 3;
    }

    public ArrayList<Position> getValidMoves() {
        // TODO Auto-generated method stub
        return null;
    }

}
