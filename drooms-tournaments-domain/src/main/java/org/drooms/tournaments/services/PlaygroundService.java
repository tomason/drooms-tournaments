package org.drooms.tournaments.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.drooms.tournaments.domain.Playground;

/**
 * This service provides access to Playgrounds on the server.
 */
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PlaygroundService {

    /**
     * Retrieves all the playgrounds on server.
     * 
     * @return Collection of games on the server.
     */
    @GET
    @Path("/playgrounds")
    public Collection<Playground> getPlaygrounds();

    /**
     * Retrieves the playgrounds created by the authenticated user.
     * 
     * @return Collection of user's playgrounds.
     */
    @GET
    @Path("/auth/playgrounds")
    public Collection<Playground> getUserPlaygrounds();

    /**
     * Stores a new playground on the server.
     * 
     * @param playground
     *            New playground.
     */
    @POST
    @Path("/auth/playgrounds")
    public void newPlayground(Playground playground);

}
