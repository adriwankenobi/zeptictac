package com.acerete.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ServicesException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4684802429947013235L;
	
	public ServicesException(String message){
		super(message);
	}
	
	public ServicesException(String baseMessage, String message){
		super(baseMessage, message);
	}
	
	@Override
	public int getErrorCode() {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
