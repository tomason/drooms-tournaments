package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Game;

@Path("/services/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameService {

    @GET
    public Response getGames();

    @GET
    @Path("/auth")
    public Response getUserGames();

    @GET
    @Path("/auth/execution")
    public Response getExecutionQueue();

    @PUT
    @Path("/auth/execution")
    public Response reserveExecution(Game game);

    @POST
    @Path("/auth/execution")
    public Response deliverResults();

}
