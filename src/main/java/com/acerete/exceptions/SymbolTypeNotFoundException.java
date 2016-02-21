package com.acerete.exceptions;

public class SymbolTypeNotFoundException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2993078763407009418L;
	
	public static final String BASE_ERROR_MSG = "Unknown symbolType with id";

	public SymbolTypeNotFoundException() {
		super(BASE_ERROR_MSG);
	}
	
	public SymbolTypeNotFoundException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
