package com.acerete.exceptions;

public class WrongTurnException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6903399419059963727L;
	
	public static final String BASE_ERROR_MSG = "Game has already finished";

	public WrongTurnException() {
		super(BASE_ERROR_MSG);
	}
	
	public WrongTurnException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
