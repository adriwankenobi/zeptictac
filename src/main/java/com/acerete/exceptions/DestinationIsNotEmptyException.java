package com.acerete.exceptions;

public class DestinationIsNotEmptyException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5116085347255530786L;
	
	public static final String BASE_ERROR_MSG = "Destination cell is not empty";

	public DestinationIsNotEmptyException() {
		super(BASE_ERROR_MSG);
	}
	
	public DestinationIsNotEmptyException(String message) {
		super(BASE_ERROR_MSG, message);
	}
}
