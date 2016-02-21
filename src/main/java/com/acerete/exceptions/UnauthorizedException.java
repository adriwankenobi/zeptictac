package com.acerete.exceptions;

import javax.servlet.http.HttpServletResponse;

public class UnauthorizedException extends ServicesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3358448694826951875L;
	
	public static final String BASE_ERROR_MSG = "Unauthorized user";

	public UnauthorizedException() {
		super(BASE_ERROR_MSG);
	}
	
	public UnauthorizedException(String message) {
		super(BASE_ERROR_MSG, message);
	}
	
	@Override
	public int getErrorCode() {
		return HttpServletResponse.SC_UNAUTHORIZED;
	}
}
