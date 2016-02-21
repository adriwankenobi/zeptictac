package com.acerete.exceptions;

public class TooManyPlayersException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String BASE_ERROR_MSG = "Too many players";

	public TooManyPlayersException() {
		super(BASE_ERROR_MSG);
	}
	
	public TooManyPlayersException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
