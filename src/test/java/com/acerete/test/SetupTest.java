package com.acerete.test;

import java.util.List;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.BeforeClass;

import com.acerete.config.ZeptictacConfig;
import com.acerete.rest.config.RESTfulResourcesConfig;
import com.acerete.services.ZeptictacServiceImpl;
import com.acerete.vo.Mark;

public class SetupTest extends JerseyTest {

	@Before
	public void before() {
		ZeptictacServiceImpl.getInstance().clearModel();
	}
	
	@BeforeClass
	public static void beforeClass() {
		ZeptictacConfig.init(true);
	}
	
	@Override
	protected Application configure() {
		ResourceConfig rc = new ResourceConfig();
		RESTfulResourcesConfig.registerAll(rc);
		return rc;
	}
	
	@Override
    protected void configureClient(final ClientConfig config) {
		// Method PATCH is not supported by Jersey client by default
		// This property enables the client to use unsupported methods
        config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
    }
	
	protected void putMarks(String gameId, List<Mark> marks) {
		for (Mark mark : marks) {
			ZeptictacServiceImpl.getInstance().putMarkInGameByPlayerId(gameId, mark);
		}
	}
}
