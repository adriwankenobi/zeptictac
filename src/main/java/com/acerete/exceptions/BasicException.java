package com.acerete.exceptions;

import javax.servlet.http.HttpServletResponse;

public class BasicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4634806654094041554L;
	
	public static final int DEFAULT_ERROR_CODE = HttpServletResponse.SC_BAD_REQUEST;
	
	public BasicException(String message){
		super(message);
	}
	
	public BasicException(String message, Throwable cause){
		super(message, cause);
	}
	
	public BasicException(String baseMessage, String message){
		super(getErrorMessage(baseMessage, message));
	}
	
	public int getErrorCode() {
		return DEFAULT_ERROR_CODE;
	}
	
	private static String getErrorMessage(String baseMessage, String message){
		StringBuilder sb = new StringBuilder(baseMessage);
		sb.append(": ").append(message);
		return sb.toString();
	}
}
