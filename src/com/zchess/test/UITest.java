package com.zchess.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.zchess.gameplay.GameMode;
import com.zchess.ui.NewGameDialog;

@SuppressWarnings("serial")
public class UITest extends JFrame implements ActionListener {
	
	String s1,s2;
	boolean undo;
	String type;
	
	private JButton ngameB;
	private NewGameDialog newGame;
	private JButton undoB;
	private JLabel bpLabel;
	private JLabel gType;
	
	private JTextField tf;
	public UITest(String string) {
		super(string);
		
		setSize(400,400);
		setLayout(new FlowLayout());
		setLocation(200, 200);
		this.setLayout(new FlowLayout());
		
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		UITest t = new UITest("UITest");
		t.run();
	}
	
	private void run() {
		
		ngameB = new JButton("New Game");
		ngameB.addActionListener(this);
		add(ngameB);
		
		undoB = new JButton("Undo");
		add(undoB);
		undoB.setEnabled(false);
		
		new JLabel();
		String mode = GameMode.BLITZ;
		bpLabel = new JLabel(mode);
		add(bpLabel);
		setVisible(true);
		JOptionPane.showMessageDialog(this, "Clock will start");
		for(int i=0; i<20; i++) {
			String s = JOptionPane.showInputDialog("Enter a name...FAST!! time is running...");
			System.out.println(s);
		}
		newGame = new NewGameDialog(this);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals(ngameB.getText())) {
			newGame.setVisible(true);
			System.out.print("newgame var assigned:");
		}
		
		if(cmd.equals(NewGameDialog.START_BUTTON_CMD)) {
			boolean start = false;
			s1 = newGame.getWhitePlayer();
			s2 = newGame.getBlackPlayer();
			undo = newGame.isUndoAllowed();
			type = newGame.gameMode();
			if(s1.equals("") || s2.equals("")) {
				//JOptionPane.showMessageDialog(newGame,"Enter names for both players.","Empty Field Error",JOptionPane.ERROR_MESSAGE);
			}
			else {
				start = true;
				newGame.dispose();
			}
			if(start) {
				tf.setText("White Player : " + s1);
				bpLabel.setText("Black Player : " + s2);
				undoB.setEnabled(undo);
				gType.setText("Game Type : " + type);
			}
			this.revalidate();
		}
	}

}
