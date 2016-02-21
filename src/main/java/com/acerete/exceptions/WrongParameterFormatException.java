package com.acerete.exceptions;

public class WrongParameterFormatException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3553314222085371907L;
	
	public static final String BASE_ERROR_MSG = "Wrong parameter format";

	public WrongParameterFormatException() {
		super(BASE_ERROR_MSG);
	}
	
	public WrongParameterFormatException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
