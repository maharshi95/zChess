package com.zchess.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

import com.zchess.pieces.Chessmen;
import com.zchess.pieces.Pawn;
import com.zchess.pieces.Queen;
import com.zchess.pieces.Rook;
import com.zchess.gameplay.Chessboard;
import com.zchess.gameplay.Color;
import com.zchess.gameplay.GameMode;
import com.zchess.gameplay.GameStatus;
import com.zchess.movements.Move;
import com.zchess.movements.Position;
import com.zchess.players.ZAgent;
import com.zchess.timing.ChessClock;
import com.zchess.timing.TimeoutEvent;
import com.zchess.timing.TimeoutObserver;
import net.miginfocom.swing.MigLayout;
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements ActionListener {
		
	//Game Elements
	private Chessboard board;
	private ZAgent whiteP;
	private ZAgent blackP;
	private ZAgent currP;
	private ChessClock clock;
	private TimeoutObserver timeoutTrigger;
	private Timer replayTimer;
	private TimerTask replayTask;
	
	//Used for keeping a track of list of Moves
	private ArrayList <Move> moveList;
	
	private String whitePlayerName;
	private String blackPlayerName;
	private String gameMode;
	private String gameStatus;
	
	//boolean variable describing characteristic of the game
	private boolean gameRunning;
	private boolean gameStarted;
	//private boolean moveSelected;
	private boolean replaying;
	private boolean undoAllowed;
	private boolean disposeCall;
	private boolean clocked;
	private boolean vsAgent;

	//GUI elements
	private JLabel whiteL;
	private JLabel blackL;
	private JLabel gameL;
	private JLabel whitePlayerL;
	private JLabel blackPlayerL;
	private JLabel gameModeL;
	private JLabel statusL;
	private JLabel screenL;
	
	private JButton newGameB;
	private JButton resetB;
	private JButton undoB;
	private JButton replayStopB;
	@SuppressWarnings("unused")
	private JButton saveB;
	@SuppressWarnings("unused")
	private JButton loadB;
	
	private JPanel topPanel;
	private JPanel statusPanel;
	private JPanel contentPane;
	
	private GScreen arena;
	private MoveLogField moveLogs ;
	private Position p_touch;
	private Position p_move;
//	private Move selectedMove;
	private NewGameDialog newGameDialog;
	
	public GameFrame() {
		
		super(FRAME_TITLE);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(FRAME_LOCATION_X, FRAME_LOCATION_Y);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		setIconImage(GAME_ICON.getImage());
		setLocation(FRAME_LOCATION_X, FRAME_LOCATION_Y);
		
		
		initialize();
		setupGUI();
		setupFrameActions();
		setVisible(true);
	}
	
	private void initialize() {
		
		board = new Chessboard();
		gameStatus = GameStatus.NOT_STARTED;
		clock = new ChessClock();
		moveList = new ArrayList<Move>();
	}
	
	private void setupGUI() {
		
		//this.setUndecorated(true);
		//this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		//changeUI();
		setupComponents();
		setupPanels();
		pack();
	}
	
	@SuppressWarnings("unused")
	private void changeUI() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	private void setupComponents() {
		
		newGameB = new JButton(NEW_GAME_TXT);
		
		resetB = new JButton(RESET_TXT);
		resetB.setEnabled(false);
		
		undoB = new JButton(UNDO_TXT);
		undoB.setEnabled(false);
		
		replayStopB = new JButton(ABORT_TXT);
		replayStopB.setEnabled(false);
		
		whiteL = new JLabel(WHITE_TXT, ARROW_ICON,JLabel.LEADING);
		whiteL.setFont(GFonts.LABEL_FONT);
		
		blackL = new JLabel(BLACK_TXT,ARROW_ICON, JLabel.LEADING);
		blackL.setFont(GFonts.LABEL_FONT);
		
		whitePlayerL = new JLabel();
		whitePlayerL.setFont(GFonts.LABEL_FONT);
		
		blackPlayerL = new JLabel();
		blackPlayerL.setFont(GFonts.LABEL_FONT);
		
		gameL = new JLabel(GAME_MODE_TXT);
		gameL.setFont(GFonts.LABEL_FONT);
		
		gameModeL = new JLabel();
		gameModeL.setFont(GFonts.LABEL_FONT);
		
		screenL = new JLabel(SCREEN_LABEL);
		screenL.setFont(GFonts.LABEL_FONT);
		
		statusL = new JLabel(DEFAULT_STATUS);
		statusL.setFont(GFonts.LABEL_FONT);
		
		arena = new GScreen(GSCREEN_WIDTH, GSCREEN_HEIGHT, board);
		moveLogs = new MoveLogField(LOG_WIDTH, LOG_HEIGHT);
	}
	
	private void setupPanels() {
		
		//Top Panel Design and Layout
		topPanel = new JPanel(new MigLayout("", "[]20[]20[]20[]", ""));
		topPanel.add(newGameB);
		topPanel.add(resetB);
		topPanel.add(undoB);
		topPanel.add(replayStopB);
		topPanel.setBackground(GColors.TOP_PANEL_BG);
		
		JLabel movesL = new JLabel("Moves :");
		movesL.setFont(GFonts.LABEL_FONT);
		
		JPanel rightPanel = new JPanel(new MigLayout ("wrap 2", "[]20[]", "[][]15[][][]20[]"));
		
		rightPanel.add(movesL, "wrap");
		rightPanel.add(moveLogs, "span 2, wrap");
		
		rightPanel.add(blackL, "gapleft 5");
		rightPanel.add(blackPlayerL, "");
		
		rightPanel.add(clock, "span 2, growx, wrap, shrink 0");
		
		rightPanel.add(whiteL, "gapleft 5");
		rightPanel.add(whitePlayerL, "");
		
		rightPanel.add(gameL, "gapleft 5");
		rightPanel.add(gameModeL, "");
		
		JPanel leftPanel = new JPanel(new MigLayout());
		
		leftPanel.add(screenL,"wrap");
		leftPanel.add(arena);
		
		statusPanel = new JPanel();
		statusPanel.add(statusL);
		
		topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		leftPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rightPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new MigLayout("fill"));
		contentPane.add(topPanel, "growx, span 2, wrap");
		contentPane.add(leftPanel,"grow");
		contentPane.add(rightPanel, "grow, wrap");
		contentPane.add(statusPanel,"growx, span 2");
		
		JPanel rankPanel = new JPanel(new GridLayout(8,1));
		for(int i=8; i>=1; i--) {
			JButton l = new JButton(""+i);
			l.setFont(GFonts.LABEL_FONT);
			l.setEnabled(false);
			rankPanel.add(l);
		}
		
		JPanel filePanel = new JPanel(new GridLayout(1,8));
		for(int i=0; i<8; i++){
			JButton b = new JButton(""+Character.toString((char)(i+'a')));
			b.setFont(GFonts.LABEL_FONT);
			b.setEnabled(false);
			filePanel.add(b);
		}
	}
	
	private void setupFrameActions() {
		
		newGameDialog = new NewGameDialog(GameFrame.this);
		replayTimer = new Timer();
		    
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {
				
				if(disposeCall == false) {
					disposeCall = true;
					JLabel exitLabel = new JLabel(AFTER_EXIT_MSG);
					 JOptionPane.showMessageDialog(GameFrame.this, exitLabel , 
								FRAME_TITLE, JOptionPane.INFORMATION_MESSAGE, GAME_ICON);
					 dispose();
				}
				replayTimer.cancel();
			}
			
			public void windowClosing(WindowEvent arg0) {
				JLabel label = new JLabel(EXIT_CONFIRM_MSG);
				int opt = JOptionPane.showConfirmDialog(GameFrame.this, label, 
						FRAME_TITLE, JOptionPane.YES_NO_OPTION);
				if(opt == JOptionPane.YES_OPTION) {
					dispose();
				}
			}

			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		
		//Button available all the time till the application window is open
		newGameB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				if(gameRunning) {
					int opt = JOptionPane.showConfirmDialog(null, NEW_GAME_CONFIRM_MSG, 
							ABORT_GAME_CONFIRM_TITLE, JOptionPane.YES_NO_OPTION);
					if(opt == JOptionPane.YES_OPTION) {
						abortGame();
						newGameDialog.setVisible(true);
						if(gameStarted) {
							gameStarted = false;
							startNewGame();
						}
					}
				}
				else {
					newGameDialog.setVisible(true);
					if(gameStarted) {
						gameStarted = false;
						startNewGame();
					}
				}
				
			}
		});
		
		//Button only available once game starts...and the availability terminates as the game aborts
		resetB.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent ae) {
				
				if(gameRunning) {
					int opt = JOptionPane.showConfirmDialog(null, RESET_GAME_CONFIRM_MSG, 
							RESET_GAME_CONFIRM_TITLE, JOptionPane.YES_NO_OPTION);
					if(opt == JOptionPane.YES_OPTION) {
						abortGame();
						startNewGame();
					}
				}
				else {
					startNewGame();
				}
			}
		});
		
		//is an abort button if game is running
		//is a replay button if game is aborted and not replaying
		//is a stop button if game is replaying
		replayStopB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(gameRunning) {
					int opt = JOptionPane.showConfirmDialog(null, ABORT_GAME_CONFIRM_MSG, 
							ABORT_GAME_CONFIRM_TITLE, JOptionPane.YES_NO_OPTION);
					if(opt == JOptionPane.YES_OPTION) {
						abortGame();
					}
					
				}
				else if(replaying) stopReplay();
				else startReplay();
			}
			
		});
		
		//available only when the game is runningand in No_clock mode
		undoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//undo is pressed when the turn is over
					//Opponent's turn going on
					currP.getOpponent().finalTakeBack();
					moveList.remove(moveList.size() - 1);
					moveLogs.undo();
					arena.rePaint();
					toggleTurn();
				} catch (RuntimeException ex) {
					JOptionPane.showMessageDialog(GameFrame.this, "Further Undo not possible");
				}
			}
		});
		
		timeoutTrigger = new TimeoutObserver() {

			public void notify(TimeoutEvent e) {
				String gameEndMsg = "";
				ImageIcon displayIcon = TIMER_ICON;
				
				if(e == TimeoutEvent.WHITE_TIME_OUT) {
					gameStatus = GameStatus.WHITE_TIMEOUT;
					gameEndMsg = blackP.name() + " wins!!";
				}
				else if(e == TimeoutEvent.BLACK_TIME_OUT) {
					gameStatus = GameStatus.BLACK_TIMEOUT;
					gameEndMsg = whiteP.name() + " wins!!";
				}
				gameEndMsg = "<html><h3>" + gameStatus + "<br>" + gameEndMsg + "</p></html";
				statusL.setText(gameStatus);
				abortGame();
				JOptionPane.showMessageDialog(GameFrame.this ,gameEndMsg, "Game Over", JOptionPane.INFORMATION_MESSAGE,displayIcon);
				
			}
			
		};
		
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		
		if(cmd.equals(NewGameDialog.START_BUTTON_CMD)) {
		
			whitePlayerName = newGameDialog.getWhitePlayer();
			blackPlayerName = newGameDialog.getBlackPlayer();
			undoAllowed = newGameDialog.isUndoAllowed();
			vsAgent = newGameDialog.isSinglePlayer();
			gameMode = newGameDialog.gameMode();
			
			clocked = true;
			if(gameMode == GameMode.NO_CLOCK)
				clocked = false;
			
			if(whitePlayerName.equals("") || blackPlayerName.equals("")) {
				JOptionPane.showMessageDialog(newGameDialog, INVALID_PLAYER_MSG,
						EMPTY_FIELD_ERROR, JOptionPane.ERROR_MESSAGE);
			}
			
			else if(whitePlayerName.equals(blackPlayerName)) {
				JOptionPane.showMessageDialog(newGameDialog, INVALID_PLAYER_MSG,
						EMPTY_FIELD_ERROR, JOptionPane.ERROR_MESSAGE);
			}
			else {
				gameStarted = true;
				newGameDialog.dispose();
			}
		}
	}
	
	private void startNewGame() {
		
		gameRunning = true;
		
		//setting up gamePlay elements
		board.reset();
		whiteP = new ZAgent(whitePlayerName, Color.WHITE, board);
		blackP = new ZAgent(blackPlayerName, Color.BLACK, board);
		whiteP.setOpponent(blackP);
		blackP.setOpponent(whiteP);
		currP = whiteP;
		if(vsAgent)
			blackP.giveIntelligence();
		
		//setting up GUI elements
		whitePlayerL.setText(": "+whiteP.name());
		blackPlayerL.setText(": "+blackP.name());
		
		whiteL.setIcon(TURN_ARRORW_ICON);
		blackL.setIcon(ARROW_ICON);
		
		gameModeL.setText(": "+gameMode);
		
		moveList.clear();
		moveLogs.reset();
		
		arena.rePaint();
		revalidate();
		
		//setting up action elements
		undoB.setEnabled(undoAllowed);
		resetB.setEnabled(true);
		
		replayStopB.setEnabled(true);
		replayStopB.setText(ABORT_TXT);
		arena.addMouseListener(mouseListener);
		
		//handling clock function
		if(clocked) {
			clock.setMode(gameMode);
			clock.setTimeoutObserver(timeoutTrigger);
			JOptionPane.showMessageDialog(this, GameMode.info(gameMode));
			JOptionPane.showMessageDialog(this, "The Clock will start now...");
			clock.start();
		}
		
		//game begins
	}
	
	private void abortGame() {
		arena.removeMouseListener(mouseListener);
		
		undoB.setEnabled(false);
		replayStopB.setText(REPLAY_TXT);
		replayStopB.setEnabled(true);
		if(clocked)
			clock.stop();
		gameRunning = false;
	}
	
	//called by startReplayMethod to assigna replay Task to the replayTimer before starting the replay
	private void assignReplayTask() {
		replayTask = new TimerTask() {
			int i = 0;
		    public void run() {  
		    	
		    	if(i < moveList.size()) {
		    	  Move m = moveList.get(i++);
		    	  currP.finalMove(m);
		    	  arena.rePaint();
		    	  toggleTurn();
		    	}
		    	else 
		    	  stopReplay();
		    }
	    };
	}
	
	//only performed if game is in aborted state and Replay button is Pressed
	private void startReplay() {
		
		resetB.setEnabled(false);
		newGameB.setEnabled(false);
		
		replayStopB.setText(STOP_TXT);
		
		currP = whiteP;
		board.reset();
		whiteP.setupPieces();
		blackP.setupPieces();
		arena.rePaint();
		
		assignReplayTask();
		replaying = true;
		replayTimer.schedule(replayTask, 600, 600);
	}
	
	//only performed in aborted state when stop button is pressed while replaying or replay gets over by itself
	private void stopReplay() {
		
		newGameB.setEnabled(true);
		resetB.setEnabled(true);
		replayStopB.setText(REPLAY_TXT);
		replaying = false;
		replayTask.cancel();
	}
	
	private void toggleTurn() {
		if(currP == whiteP) {
			currP = blackP; 
			blackL.setIcon(TURN_ARRORW_ICON);
			whiteL.setIcon(ARROW_ICON);
		}
		else {
			currP = whiteP; 
			whiteL.setIcon(TURN_ARRORW_ICON);
			blackL.setIcon(ARROW_ICON);
		}
		if(clocked)
			clock.switchTurn();
		revalidate();
	}
	
	private MouseListener mouseListener = new MouseListener() {
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		
		public void mousePressed(MouseEvent e) {
			if(gameRunning && currP.isHuman() && e.getSource().equals(arena)) {
				Position p = arena.position(e.getX(), e.getY());
				ArrayList<Move> moves = new ArrayList<Move>();
				for(Move m :  currP.allLegalMoves()) {
					if(p.equals(m.initialPosition()))
						moves.add(m);
				}
				arena.showMoveHighLights(p, moves);
				p_touch = p;
			}
		}
		
		//Contains the whole GamePlay Logic
		public void mouseReleased(MouseEvent e) {
			boolean moved = false;
	
			//if clicked on the GScreen..recording the Move...
			if(gameRunning && currP.isHuman() && e.getSource().equals(arena)) {
				arena.hideMoveHighlights();
				p_move = arena.position(e.getX(), e.getY());
				Move move = new Move(p_touch, p_move);
				
				//if recorded move was legal for current Player
				if(currP.isLegal(move)) {
					
					String pname = " ";
					
					//Handling PawnPromotion seperately by popping Dialog
					if(currP.isPawnPromotion(move)) {
						
						//preparing the Dialog for Pawn Promotion
						Object[] list = {QUEEN_ICON, ROOK_ICON, BISHOP_ICON, KNIGHT_ICON};
						int opt = JOptionPane.showOptionDialog(GameFrame.this, "Promote pawn to :",
								"Pawn Promotion Dialog", JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE,
								null, list, list[0]);
						
						//Checking Users input
						if(opt != JOptionPane.CLOSED_OPTION) {
							switch(opt) {
							case 0:
								pname = "Q";
								break;
							case 1:
								pname = "R";
								break;
							case 2:
								pname = "B";
								break;
							case 3:
								pname = "N";
								break;
							}
							//promotion occurs only if user clicks one of the Icon Buttons
							move.setPromotion(pname);
							moveAction(move);
							moved = true;
						}
						//If close button pressed turn remains same
						
					}
					//Condtion for normal Legal move, Making the final move and toggling turn
					else {
						moveAction(move);
						moved = true;
					}
				}
				if(gameRunning && moved && currP.isEngine()) {
					arena.hideMoveHighlights();
					move = currP.getBestMove();
					moveAction(move);
				}
			}
		}
	};
	
	//everything is finalized and this method gets called when move is ready to be taken;
	private void moveAction(Move move) {
		
		moveLogs.addMove(move, currP.pieceMoved(move));
		currP.finalMove(move);
		
		arena.rePaint();
		arena.hideAlertHighlight(board.positionOf(currP.king()));
		
		String status = currP.toString() + ":  "+move.toString();
		
		if(currP.getOpponent().isUnderCheck()) {
			status += "  CHECK!!";
			arena.showAlertHighLight(board.positionOf(currP.getOpponent().king()));
		}
		
		statusL.setText(status);
		moveList.add(move);
		toggleTurn();
		checkGameOver();
	}
	
	private boolean insufficientPieces() {
		ArrayList<Chessmen> pieces = whiteP.alivePieces();
		pieces.addAll(blackP.alivePieces());
		boolean insuff = true;
		for(Chessmen piece : pieces) {
			if (piece instanceof Queen
					|| piece instanceof Rook
					|| piece instanceof Pawn) {
				insuff = false;
			}
		}
		if(insuff) {
			if(whiteP.bishops().size() == 2
					|| blackP.bishops().size() == 2)
				insuff = true;
		}
		return insuff;
	}
	
	public void checkGameOver() {
		if((currP.isMate() || insufficientPieces())) {
			
			String gameEndMsg = "";
			ImageIcon displayIcon;
			if(insufficientPieces() && gameMode != GameMode.ARMAGEDDON) {
				gameStatus = GameStatus.NOT_ENOUGH_PIECES;
				gameEndMsg = "Game Draw";
				displayIcon = DRAW_ICON;
			}
			else if(currP.isStaleMate() && gameMode != GameMode.ARMAGEDDON) {
				gameStatus = GameStatus.STALEMATE;
				gameEndMsg = "Game Draw";
				displayIcon = DRAW_ICON;
			}
			else if(currP.isWhite() && currP.isCheckMate()) {
				gameStatus = GameStatus.WHITE_CHECKMATE;
				gameEndMsg = blackPlayerName + " wins!!";
				displayIcon = WHITE_CM_ICON;
			}
			else if(currP.isBlack() && currP.isCheckMate()){
				gameStatus = GameStatus.BLACK_CHECKMATE;
				gameEndMsg = whitePlayerName + " wins!!";
				displayIcon = BLACK_CM_ICON;
			}
			else {
				gameStatus = GameStatus.ARMAGEDDON_WIN;
				gameEndMsg = blackPlayerName + " wins!!";
				displayIcon = WHITE_CM_ICON;
			}
			
			gameEndMsg = "<html><h3>" + gameStatus + "<br>" + gameEndMsg + "</p></html";
			statusL.setText(gameStatus);
			JOptionPane.showMessageDialog(GameFrame.this ,gameEndMsg, "Game Over", JOptionPane.INFORMATION_MESSAGE,displayIcon);
			abortGame();
		}
	}

	
	public static final Toolkit tk = Toolkit.getDefaultToolkit();
	public static final Dimension screenD = tk.getScreenSize();
	
	public static final int FRAME_WIDTH = 950;
	public static final int FRAME_HEIGHT = 500;
	
	public static final int GSCREEN_WIDTH = 500;
	public static final int GSCREEN_HEIGHT = GSCREEN_WIDTH;
	
	public static final int LOG_WIDTH = 300;
	public static final int LOG_HEIGHT = 300;
	
	@SuppressWarnings("unused")
	private static final int TOP_PANEL_VER_OFFSET = 10;
	@SuppressWarnings("unused")
	private static final double WIDTH_FRACTION = 0.65;
	
	public static final int FRAME_LOCATION_X = 100;
	public static final int FRAME_LOCATION_Y = 30;
	
	
	
	private static final String FRAME_TITLE = "ZChess v2.4";
	
	private static final String NEW_GAME_TXT = "New Game";
	private static final String UNDO_TXT = "Undo";
	private static final String RESET_TXT = "Reset Game";
	private static final String REPLAY_TXT = "Replay";
	private static final String STOP_TXT = "Stop";
	private static final String ABORT_TXT = "Abort";
	
	private static final String WHITE_TXT = "White ";
	private static final String BLACK_TXT = "Black ";
	private static final String GAME_MODE_TXT = "Mode ";
	private static final String SCREEN_LABEL = "Gameplay Region: ";
	
	private static final String DEFAULT_STATUS = "Hello.... Welcome to ZChess v2.4";
	
	private static final String INVALID_PLAYER_MSG = "Enter names for both players.";
	private static final String EMPTY_FIELD_ERROR = "Empty Field Error";
	
	private static final String NEW_GAME_CONFIRM_MSG = "A game is already running. \nDo you want to abort this \ngame and start a New one ?";
	protected static final Object ABORT_GAME_CONFIRM_MSG = "Are you sure you want to abort this game ?";
	private static final String RESET_GAME_CONFIRM_MSG = "Are you sure you want to reset this game ?";
	
	private static final String RESET_GAME_CONFIRM_TITLE = "Reset game confirmation";
	private static final String ABORT_GAME_CONFIRM_TITLE = "Abort game confirmation";
	
	private static final String EXIT_CONFIRM_MSG = "Really Exit ?";
	private static final String AFTER_EXIT_MSG = "<html><h3>Thankyou for Playing ZChess v2.4<br>Newer Versions will be out soon...  :)";
	
	private static final String piece_url = "resource/ChessPiece/White/";
	private static final String icon_url = "resource/icons/";
	
	@SuppressWarnings("unused")
	private static final ImageIcon KING_ICON = new ImageIcon(piece_url+"King.png");
	private static final ImageIcon QUEEN_ICON = new ImageIcon(piece_url+"Queen.png");
	private static final ImageIcon ROOK_ICON = new ImageIcon(piece_url+"Rook.png");
	private static final ImageIcon BISHOP_ICON = new ImageIcon(piece_url+"Bishop.png");
	private static final ImageIcon KNIGHT_ICON = new ImageIcon(piece_url+"Knight.png");
	@SuppressWarnings("unused")
	private static final ImageIcon PAWN_ICON = new ImageIcon(piece_url+"Pawn.png");
	
	private static final ImageIcon WHITE_CM_ICON = new ImageIcon(icon_url+"white_cm.png");
	private static final ImageIcon BLACK_CM_ICON = new ImageIcon(icon_url+"black_cm.png");
	private static final ImageIcon TIMER_ICON = new ImageIcon(icon_url+"timer.png");
	private static final ImageIcon DRAW_ICON = new ImageIcon(icon_url+"draw.png");
	
	private static final ImageIcon TURN_ARRORW_ICON = new ImageIcon(icon_url+"arrow_turn.png");
	private static final ImageIcon ARROW_ICON = new ImageIcon(icon_url+"arrow.png");
	
	private static final ImageIcon GAME_ICON = new ImageIcon(icon_url+"main.png");;
	
	@SuppressWarnings("unused")
	private static final LayoutManager FRAME_LAYOUT = new BorderLayout();	

}
