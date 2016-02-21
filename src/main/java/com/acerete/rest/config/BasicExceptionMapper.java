package com.acerete.rest.config;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.acerete.exceptions.BasicException;

public class BasicExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {
		
		BasicException genEx = null;
		if (ex instanceof BasicException) {
			genEx = (BasicException) ex;
		}
		else {
			genEx = new BasicException(ex.getMessage(), ex);
		}

		return Response.status(genEx.getErrorCode())
				.entity(genEx.getMessage())
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
