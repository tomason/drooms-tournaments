package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Game;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;

/**
 * @deprecated This service is for testing purposes only and will be removed once Tournament and Scheduling features are implemented.
 */
@Deprecated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameManagementService {

    @GET
    @Path("/players")
    @ClientResponseType(entityType = List.class)
    public Response getAllPlayers();

    @GET
    @Path("/playgrounds")
    @ClientResponseType(entityType = List.class)
    public Response getAllPlaygrounds();

    @GET
    @Path("/strategy/{user}")
    @ClientResponseType(entityType = Strategy.class)
    public Response getActiveStrategy(@PathParam("user") String userName);

    @POST
    @Path("/insert")
    @ClientResponseType(entityType = Void.class)
    public Response newGame(Game game);

}
