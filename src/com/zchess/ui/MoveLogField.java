package com.zchess.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.zchess.pieces.Chessmen;
import com.zchess.gameplay.Color;
import com.zchess.movements.Move;

@SuppressWarnings("serial")
public class MoveLogField extends JPanel {
	
	private JTextArea moveLog;
	private JScrollPane scrollPane;
	private int lines;
	private boolean whiteTurn;
	private ArrayList<String> moves;
	private int width;
	private int height;
	
	public MoveLogField(int w, int h) {
		width = w;
		height = h;
		lines = 0;
		whiteTurn = true;
		moves = new ArrayList<String>(20);
		prepareGUI();
	}
	
	private void prepareGUI() {
		
		moveLog = new JTextArea();
		moveLog.setBackground(GColors.CONSOLE_BG);
		moveLog.setFont(GFonts.CONSOLE_FONT);
		moveLog.setEditable(false);
		
		scrollPane = new JScrollPane(moveLog);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black, 2, false));
		scrollPane.setPreferredSize(new Dimension(width,height));
		add(scrollPane);
	}
	
	public void addMove(Move move, Chessmen piece) {
		
		if(whiteTurn) {
			String str = "";
			str += ++lines + ". " + "White: " + piece.name() + " " + move.toString();
			moves.add(str);
			refresh();
			whiteTurn = false;
		}
		
		else {
			String str = moves.get(moves.size() - 1);
			str += "   " + "Black: " + piece.name() + " " + move.toString() + "\n";
			moves.set(moves.size()-1, str);
			refresh();
			whiteTurn = true;
		}
	}
	
	private void refresh() {
		String text = "";
		for(String str : moves) {
			text += str;
		}
		moveLog.setText(text);
	}
	
	public void reset() {
		lines = 0;
		whiteTurn = true;
		moves.clear();
		refresh();
	}

	public void undo() {
		if(whiteTurn) {
			String str = moves.get(moves.size() - 1);
			int i = str.indexOf("Black");
			str = str.substring(0, i-3);
			moves.set(moves.size()-1, str);
			whiteTurn = false;
			
		}
		else {
			moves.remove(moves.size() -1);
			lines--;
			whiteTurn = true;
		}
		refresh();
	}
	
}
