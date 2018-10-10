package com.zchess.timing;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownTimer implements Runnable {

	private Timer timer;
	private TimerTask task;
	private int interval;
	private int total_secs;
	private int period;
	private String label;
	
	private CountDownTimer curr;
	
	public CountDownTimer(String name, int secs) {
	    timer = new Timer();
	    total_secs = secs;
	    interval = secs;
	    label = name;
	    curr = this;
	    period = 1000;
	    task = new TimerTask() {
			public void run() {
				System.out.println(curr.toString());
				setInterval();
			}
		};
	}
	
	public CountDownTimer(String name, int secs, int per) {
	    timer = new Timer();
	    total_secs = secs;
	    interval = secs;
	    label = name;
	    curr = this;
	    period = per;
	    task = new TimerTask() {
			public void run() {
				System.out.println(curr.toString());
				setInterval();
			}
		};
	}
	
	public CountDownTimer(String name, TimerTask tk, int secs) {
	    timer = new Timer();
	    total_secs = secs;
	    interval = secs;
	    label = name;
	    curr = this;
	    task = tk;
	    period = 1000;
	}
	
	public CountDownTimer(String name, TimerTask tk, int secs, int per) {
	    timer = new Timer();
	    total_secs = secs;
	    interval = secs;
	    label = name;
	    curr = this;
	    task = tk;
	    period = per;
	}
	
	
	
	public void reset() {
		interval = total_secs;
	}
	
	public void run() {
		
		timer.scheduleAtFixedRate(task, period, period);
	}
	
	private final int setInterval() {
	    if (interval == 1)
	        timer.cancel();
	    return --interval;
	}
	public String toString() {
		return label + ": " + interval;
	}
}
