package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

@Path("/services/tournaments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TournamentService {

    @GET
    public Response getTournaments();

    /**
     * Lists tournaments. The resulting list contains tournaments User has
     * joined or those that are not yet ended (allowing the User to join it).
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @return The {@link List} of {@link Tournament} User participates in or
     *         the ones he can join.
     */
    @GET
    @Path("/auth")
    public Response getUserTournaments();

    /**
     * Starts a new tournament.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param tournament
     *            Tournament to be started.
     * @return Nothing.
     */
    @POST
    @Path("/auth")
    public Response newTournament(Tournament tournament);

    /**
     * Enrolls User in the Tournament.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param tournament
     *            Tournament to join.
     * @return Nothing.
     */
    @PUT
    @Path("/auth")
    public Response joinTournament(Tournament tournament);
}
