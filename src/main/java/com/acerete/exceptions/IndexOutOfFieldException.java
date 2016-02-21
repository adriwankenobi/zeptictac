package com.acerete.exceptions;

public class IndexOutOfFieldException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6183089526030364616L;
	
	public static final String BASE_ERROR_MSG = "Index out of field boundaries";

	public IndexOutOfFieldException() {
		super(BASE_ERROR_MSG);
	}
	
	public IndexOutOfFieldException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
