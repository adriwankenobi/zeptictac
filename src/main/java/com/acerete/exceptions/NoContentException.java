package com.acerete.exceptions;

import javax.servlet.http.HttpServletResponse;

public class NoContentException extends ServicesException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3402084397129690696L;
	
	public static final String BASE_ERROR_MSG = "No content exception";

	public NoContentException() {
		super(BASE_ERROR_MSG);
	}
	
	public NoContentException(String message) {
		super(BASE_ERROR_MSG, message);
	}
	
	@Override
	public int getErrorCode() {
		return HttpServletResponse.SC_NO_CONTENT;
	}
}
