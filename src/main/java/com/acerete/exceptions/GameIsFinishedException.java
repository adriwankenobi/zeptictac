package com.acerete.exceptions;

public class GameIsFinishedException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6903399419059963727L;
	
	public static final String BASE_ERROR_MSG = "Game has already finished";

	public GameIsFinishedException() {
		super(BASE_ERROR_MSG);
	}
	
	public GameIsFinishedException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
