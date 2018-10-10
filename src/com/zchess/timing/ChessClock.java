package com.zchess.timing;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import com.zchess.gameplay.GameMode;
import com.zchess.ui.GFonts;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ChessClock extends JPanel {
	
	public static final int WHITE_TURN = 0;
	public static final int BLACK_TURN = 1;
	
	private static final int ONE_SECOND = 1000;
	
	private JLabel[] label = new JLabel[2];
	
	private Timer timer = new Timer("Timer", true);
	private ClockTask clockTask;
	private long[] time = new long[2];
	private long systemTime = 0;
	private int turn = WHITE_TURN;
	private int initial_sec;
	private int inc;
	private TimeoutObserver timeoutObserver;
	private String mode;
	
	public ChessClock() {
		super();
		initGUI();
	}
	
	public void setMode(String gmode) {
		mode = gmode;
		set_init_n_inc();
		setTime(initial_sec*ONE_SECOND);
		if(mode == GameMode.ARMAGEDDON)
			time[WHITE_TURN] += 60*ONE_SECOND;
		label[turn].setText(ChessClock.this.toString(time[turn]));
		
	}
	
	public void setTimeoutObserver(TimeoutObserver to) {
		timeoutObserver = to;
	}
	
	private void set_init_n_inc() {
		if(mode == GameMode.RAPID) {
			initial_sec = 15*60;
			inc = 0;
		}
		else if(mode == GameMode.LIGHTENING) {
			initial_sec = 1*60;
			inc = 1;
		}
		else if(mode == GameMode.BLITZ) {
			initial_sec = 5*60;
			inc = 2;
		}
		else {
			initial_sec = 5*60;
			inc = 2;
		}
	}
	
	public synchronized void start() {
		long remainder = Math.abs(time[turn] % 1000);
		timer.scheduleAtFixedRate(clockTask = new ClockTask(), 
				(remainder == 0)? 1000 : remainder, ONE_SECOND);
		systemTime = System.currentTimeMillis();
	}
	
	public synchronized void stop() {
		if(clockTask != null) clockTask.cancel();
	}
	
	private void initGUI() {
		try {
			this.setLayout(new MigLayout("center, fill, wrap 1", "2[]2", "2[]2[]2"));
			for(int i : new int[] {WHITE_TURN, BLACK_TURN}) {
				label[i] = new JLabel("00:00");
				label[i].setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				label[i].setFont(GFonts.LABEL_FONT);
			}
			this.add(label[BLACK_TURN], "growx, shrink 0");
			this.add(label[WHITE_TURN], "growx, shrink 0");
			this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public void switchTurn() {
		time[turn] += inc*ONE_SECOND;
		label[turn].setText(toString(time[turn]));
		turn = (turn + 1) % 2;
		
	}
	
	private class ClockTask extends TimerTask {
		@Override
		public synchronized void run() {
			systemTime = System.currentTimeMillis();
			time[turn] -= 1000;
			label[turn].setText(ChessClock.this.toString(time[turn]));
			if(time[turn] <= 0) {
				cancel();
				if(turn == WHITE_TURN)
					timeoutObserver.notify(TimeoutEvent.WHITE_TIME_OUT);
				else
					timeoutObserver.notify(TimeoutEvent.BLACK_TIME_OUT);
			}
				
		}

		@Override
		public synchronized boolean cancel() {
			time[turn] -= System.currentTimeMillis() - systemTime;
			label[turn].setText(ChessClock.this.toString(time[turn]));
			return super.cancel();
		}
	}
	
	private String toString(long time) {
		String sign = new String();
		int timeSecond = (int)(time / 1000);
		if(timeSecond < 0) {
			timeSecond = -timeSecond;
			sign = "-";
		}
		int hour = timeSecond / 3600;
		int minute = (timeSecond % 3600) / 60;
		int second = timeSecond % 60;
		
		return (hour == 0)? String.format(sign + "%1$02d:%2$02d", minute, second)
				: String.format(sign + "%1$02d:%2$02d:%3$02d", hour, minute, second);
	}

	public long getTime(int turn) {
		return time[turn];
	}
	
	public void setTime(int turn, long time) {
		this.time[turn] = time;
		label[turn].setText(toString(this.time[turn]));
	}
	
	public void setTime(long time) {
		setTime(WHITE_TURN, time);
		setTime(BLACK_TURN, time);
	}
}