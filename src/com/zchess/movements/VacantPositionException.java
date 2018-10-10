package com.zchess.movements;

@SuppressWarnings("serial")
public class VacantPositionException extends RuntimeException {

	public VacantPositionException() {
		super();
	}

	public VacantPositionException(String message) {
		super(message);
	}

}
