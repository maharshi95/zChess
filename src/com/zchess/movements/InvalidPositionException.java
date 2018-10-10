package com.zchess.movements;

@SuppressWarnings("serial")
public final class InvalidPositionException extends RuntimeException {

	public InvalidPositionException() {
		super("Position Out of Bounds");
	}
	
	public InvalidPositionException(String arg0) {
		super(arg0);
	}

}
