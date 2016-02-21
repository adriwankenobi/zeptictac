package com.acerete.rest.config;

import org.glassfish.jersey.server.ResourceConfig;

import com.acerete.config.ZeptictacConfig;
import com.acerete.exceptions.ServerConfigException;
import com.acerete.rest.authorization.AuthorizationRequestFilter;
import com.acerete.rest.decoration.DecorationRequestResponseFilter;
import com.acerete.rest.resources.GameResource;
import com.acerete.rest.validation.ValidationRequestFilter;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class RESTfulResourcesConfig extends ResourceConfig {

	public RESTfulResourcesConfig() throws ServerConfigException {
		
		// Init server config
		ZeptictacConfig.init();
		
		// Register classes
		registerAll(this);
	}
	
	public static void registerAll(ResourceConfig rc) {
		
		// Register resources
		rc.register(GameResource.class);
				
		// Register Jackson JSON conversion
		rc.register(JacksonJaxbJsonProvider.class);
				
		// Register exception mapper
		rc.register(BasicExceptionMapper.class);
			
		// Register filters
		rc.register(AuthorizationRequestFilter.class);
		rc.register(ValidationRequestFilter.class);
		rc.register(DecorationRequestResponseFilter.class);
	}
}
