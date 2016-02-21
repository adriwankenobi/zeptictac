package com.acerete.exceptions;

public class NotEnoughPlayersException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7621912043351230578L;
	
	public static final String BASE_ERROR_MSG = "Not enough players yet";

	public NotEnoughPlayersException() {
		super(BASE_ERROR_MSG);
	}
	
	public NotEnoughPlayersException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
