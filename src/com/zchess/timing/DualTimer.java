package com.zchess.timing;

import java.util.ArrayList;

public class DualTimer implements Runnable {

	private Long wTime;
	private Long bTime;
	
	private Long wEndTime;
	private Long bEndTime;
	
	private Long timeStarted;
	
	private boolean wTurn;
	private boolean stopped;
	
	private ArrayList<TimeoutObserver> observers = new ArrayList<TimeoutObserver>(1);
	
	public DualTimer(Long time1, Long time2) {
		
		wTime = time1;
		bTime = time2;
		wTurn = true;
	}

	public void addTimeoutObserver(final TimeoutObserver to) {
	    observers.add(to);
	}

	public void removeTimeoutObserver(final TimeoutObserver to) {
	    observers.remove(to);
	}
	
	protected final void fireTimeoutOccured(TimeoutEvent e) {
	    for(TimeoutObserver to : observers) { 
	    	to.notify(e);
	    }
	  }
	
	public  void start() {
		stopped = false;
		Long now = System.currentTimeMillis();
		timeStarted = now;
		wEndTime = now + wTime;
		bEndTime = now + bTime;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(bEndTime > now && wEndTime > now) {
			now = System.currentTimeMillis();
			//System.out.println(now);
		}
		stopped = true;
		if(bEndTime <= now) {
			fireTimeoutOccured(TimeoutEvent.BLACK_TIME_OUT);
		}
		else {
			fireTimeoutOccured(TimeoutEvent.WHITE_TIME_OUT);
		}
	}
	
	public  void toggle() {
		Long now = System.currentTimeMillis();
		
		if(wTurn) 	bEndTime += now - timeStarted;
		else 	wEndTime += now - timeStarted;
		
		wTurn = !wTurn;
		timeStarted = now;
	}
	
	public  void run() {
		start();
	}
	
	public  void stop() {
		if(!stopped)
			fireTimeoutOccured(TimeoutEvent.NO_TIME_OUT);
	}
	
	public  String toString() {
		Long now = System.currentTimeMillis();
		return "" + (wEndTime - now) + " " + (bEndTime - now);
	}

}
