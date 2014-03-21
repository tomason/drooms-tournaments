package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

@Path("/services/playgrounds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PlaygroundService {

    @GET
    public Response getPlaygrounds();

    /**
     * List all the User's Playgrounds.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @return {@link List} of {@link Playground} registered for this User.
     */
    @GET
    @Path("/auth")
    public Response getUserPlaygrounds();

    /**
     * Register a new Playground for User.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param playground
     *            Playground to be registered.
     * @return Nothing.
     */
    @POST
    @Path("/auth")
    public Response newPlayground(Playground playground);

}
