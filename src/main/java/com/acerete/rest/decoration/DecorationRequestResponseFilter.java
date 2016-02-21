package com.acerete.rest.decoration;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * Support for GZIP encoded calls
 */
@Decorated
@Priority(Priorities.HEADER_DECORATOR)
public class DecorationRequestResponseFilter implements ContainerRequestFilter, WriterInterceptor {

	private static final String IS_GZIP_ENCODED = "isGzipEncoded";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String encoding = requestContext.getHeaderString("Accept-Encoding");
		boolean isGzipEncoded = encoding != null && encoding.toLowerCase().contains("gzip");
		requestContext.setProperty(IS_GZIP_ENCODED, isGzipEncoded);
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		if (context.getProperty(IS_GZIP_ENCODED) != null) {
			boolean isGzipEncoded = (Boolean) context.getProperty(IS_GZIP_ENCODED);
			if (isGzipEncoded) {
				OutputStream old = context.getOutputStream();
				GZIPOutputStream gzipos = new GZIPOutputStream(old);
				context.setOutputStream(gzipos);
				context.getHeaders().add("Content-Encoding", "gzip");
			}
		}
		context.proceed();
	}
}
