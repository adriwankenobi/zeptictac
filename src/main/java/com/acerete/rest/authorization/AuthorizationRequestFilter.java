package com.acerete.rest.authorization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.acerete.exceptions.UnauthorizedException;
import com.acerete.services.ZeptictacServiceImpl;
import com.acerete.vo.Game;
import com.acerete.vo.Mark;
import com.acerete.vo.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

@Authorized
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		// Only PUT calls need authorization (@Authorized should be annotated)
		if (requestContext.getMethod().equals("PUT")) {
			
			// Get gameId from URI parameter
			String gameId = requestContext.getUriInfo().getPathParameters().get("id").iterator().next();
			Game game = ZeptictacServiceImpl.getInstance().getGameById(gameId);
			if (game != null) {
			
				// Get JSON mark from body
				ObjectMapper mapper = new ObjectMapper();
				InputStream inputStream = requestContext.getEntityStream();
				Mark mark = mapper.readValue(inputStream, Mark.class);
				
				// Replace inputStream since Jersey has already read it
				requestContext.setEntityStream(new ByteArrayInputStream(mapper.writeValueAsBytes(mark)));
						
				// Check if this player is authorized in this game
				Iterator<Player> it = game.getPlayers().iterator();
				boolean found = false;
				while (it.hasNext()) {
					if (it.next().getId().equals(mark.getPlayerId())) {
						found = true;
						break;
					}
				}
				if (!found) {
					throw new UnauthorizedException(mark.getPlayerId());
				}
			}
		}
	}
}
