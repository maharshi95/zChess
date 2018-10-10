package com.zchess.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.zchess.gameplay.GameMode;

@SuppressWarnings("serial")
public class NewGameDialog extends JDialog {

	private JLabel whiteLabel;
	private JLabel blackLabel;
	
	private JTextField whiteText;
	private JTextField blackText;
	
	private JPanel whitePanel;
	private JPanel blackPanel;
	private JPanel playerPanel;
	private JPanel modePanel;
	private JPanel newGamePanel;
	
	private JRadioButton rapidRB;
	private JRadioButton blitzRB;
	private JRadioButton lighteningRB;
	private JRadioButton armageddonRB;
	private JRadioButton noClockRB;
	
	private JCheckBox undoCB;
	private JCheckBox playerModeCB;
	
	private JButton startB;
	
	private ButtonGroup modeGroup;
	
	private Border border;
	
	private Frame owner;
	
	private String t_name = "Player 2";
	
	public NewGameDialog(Frame owner) {
		
		super(owner,TITLE,true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		preparePanel();
		setContentPane(newGamePanel);
		setLocation(200,200);
		pack();
		this.owner = owner;
		whiteText.requestFocusInWindow();
		setupListeners();
	}
	
	private void preparePanel() {
		
		newGamePanel = new JPanel();
		
		preparePlayerPanel();
		newGamePanel.add(playerPanel);
		
		prepareGameModePanel();
		newGamePanel.add(modePanel);
		
	}
	private void preparePlayerPanel() {
		
		whiteLabel = new JLabel(WHITE_LABEL);
		blackLabel = new JLabel(BLACK_LABEL);
		
		whiteText = new JTextField(15);
		whiteText.setFont(GFonts.TXT_FIELD_FONT);
		
		blackText = new JTextField(15);
		blackText.setFont(GFonts.TXT_FIELD_FONT);
		
		whitePanel = new JPanel(new FlowLayout());
		blackPanel = new JPanel(new FlowLayout());
		
		whitePanel.add(whiteLabel);
		whitePanel.add(whiteText);
		
		blackPanel.add(blackLabel);
		blackPanel.add(blackText);
		
		playerPanel = new JPanel(new GridLayout(4,1));
		
		playerPanel.add(whitePanel);
		playerPanel.add(blackPanel);
		
		JPanel cbPanel = new JPanel();
		
		undoCB = new JCheckBox(UNDO_OPT);
		cbPanel.add(undoCB);
		
		playerModeCB = new JCheckBox(COMP_OPT);
		cbPanel.add(playerModeCB);
		
		playerPanel.add(cbPanel);
		
		startB = new JButton(START_BUTTON_CMD);
		playerPanel.add(startB);
	}
	
	private void prepareGameModePanel() {
		
		modePanel = new JPanel(new GridLayout(N_TYPES,1));
		
		border = BorderFactory.createTitledBorder(GAME_TYPE);
		
		modeGroup = new ButtonGroup();
		
		blitzRB = new JRadioButton(GameMode.BLITZ);
		rapidRB = new JRadioButton(GameMode.RAPID);
		armageddonRB = new JRadioButton(GameMode.ARMAGEDDON);
		lighteningRB = new JRadioButton(GameMode.LIGHTENING);
		noClockRB = new JRadioButton(GameMode.NO_CLOCK);
		
		modeGroup.add(rapidRB);
		modeGroup.add(blitzRB);
		modeGroup.add(lighteningRB);
		modeGroup.add(noClockRB);
		modeGroup.add(armageddonRB);
		
		rapidRB.setEnabled(true);
		blitzRB.setEnabled(true);
		lighteningRB.setEnabled(true);
		armageddonRB.setEnabled(true);
		
		noClockRB.setSelected(true);
		
		modePanel.setBorder(border);
		modePanel.add(blitzRB);
		modePanel.add(rapidRB);
		modePanel.add(lighteningRB);
		modePanel.add(noClockRB);
		modePanel.add(armageddonRB);
	}
	
	public String getWhitePlayer() {
		return whiteText.getText();
	}
	
	public String getBlackPlayer() {
		return blackText.getText();
	}
	
	public boolean isUndoAllowed() {
		return undoCB.isSelected();
	}
	
	public boolean isSinglePlayer() {
		return playerModeCB.isSelected();
	}
	
	public String gameMode() {
		if(blitzRB.isSelected())
			return GameMode.BLITZ;
		
		else if(rapidRB.isSelected())
			return GameMode.RAPID;
		
		else if(lighteningRB.isSelected())
			return GameMode.LIGHTENING;
		
		else if(noClockRB.isSelected())
			return GameMode.NO_CLOCK;
		
		else if(armageddonRB.isSelected())
			return GameMode.ARMAGEDDON;
		
		else return GameMode.INVALID_TYPE;
	}
	
	private void setupListeners() {
		
		startB.addActionListener((ActionListener) owner);
		
		undoCB.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				boolean tick = ((JCheckBox)e.getSource()).isSelected();
				if(tick) {
					noClockRB.setSelected(true);
					blitzRB.setEnabled(false);
					rapidRB.setEnabled(false);
					lighteningRB.setEnabled(false);
					armageddonRB.setEnabled(false);
				}
				else {
					blitzRB.setEnabled(true);
					rapidRB.setEnabled(true);
					lighteningRB.setEnabled(true);
					armageddonRB.setEnabled(true);
				}
			}
			
		});
		
		playerModeCB.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				boolean tick = ((JCheckBox)e.getSource()).isSelected();
				if(tick) {
					t_name = blackText.getText();
					blackText.setEditable(false);
					blackText.setText("EngineX");
				}
				else {
					blackText.setText(t_name);
					blackText.setEditable(true);
				}
			}
			
		});
		
	}
	/*public void addStartButtonListener(ActionListener actionListener) {
		startB.addActionListener(actionListener);
		setVisible(true);
	}*/
	
	private static final String WHITE_LABEL = "White Player: ";
	private static final String BLACK_LABEL = "Black Player: ";
	
	@SuppressWarnings("unused")
	private static final String WHITE_DEFAULT_NAME = "Player 1";
	@SuppressWarnings("unused")
	private static final String BLACK_DEFAULT_NAME = "Player 2";
	
	private static final String GAME_TYPE = "Mode of Game: ";
	private static final String UNDO_OPT = "Allow Undo";
	private static final String COMP_OPT = "Play Against Computer";
	private static final String TITLE = "New Game";
	
	public static final String START_BUTTON_CMD = "Start";
	private static final int N_TYPES = 5;

}
