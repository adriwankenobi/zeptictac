package com.acerete.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ServerConfigException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8342327141381414059L;
	
	public static final String BASE_ERROR_MSG = "Server config failed";

	public ServerConfigException() {
		super(BASE_ERROR_MSG);
	}
	
	public ServerConfigException(String message) {
		super(BASE_ERROR_MSG, message);
	}
	
	@Override
	public int getErrorCode() {
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	}
}
