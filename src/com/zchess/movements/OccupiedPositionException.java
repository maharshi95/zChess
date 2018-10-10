package com.zchess.movements;

@SuppressWarnings("serial")
public class OccupiedPositionException extends RuntimeException {
	
	public OccupiedPositionException() {
		super();
	}
	public OccupiedPositionException(String arg0) {
		super(arg0);
	}
}
