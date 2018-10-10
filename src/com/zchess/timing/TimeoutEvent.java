package com.zchess.timing;

public enum TimeoutEvent {
	BLACK_TIME_OUT,
	WHITE_TIME_OUT,
	NO_TIME_OUT;
	
	public String getMsg() {
		if(this.equals(BLACK_TIME_OUT)) {
			return("Black runs out of Time...");
		}
		else if(this.equals(WHITE_TIME_OUT)) {
			return("White runs out of Time...");
		}
		else {
			return("Stopped before Timeout...");
		}
	}
}
