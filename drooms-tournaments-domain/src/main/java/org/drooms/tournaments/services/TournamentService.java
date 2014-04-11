package org.drooms.tournaments.services;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.drooms.tournaments.domain.Tournament;

/**
 * This service provides access to Tournaments on the server.
 */
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TournamentService {

    /**
     * Retrieves all tournaments on the server.
     * 
     * @return Collection of tournaments on the server.
     */
    @GET
    @Path("/tournaments")
    public Collection<Tournament> getTournaments();

    /**
     * Retrieves tournament's detail (i.e. playgrounds and players enrolled).
     * 
     * @param tournamentName
     *            Name of the tournament to get details for.
     * @return Tournament with Playgrounds and TournamentResults.
     */
    @GET
    @Path("/tournaments/tournament")
    public Tournament getTournament(@QueryParam("name") String tournamentName);

    /**
     * Lists tournaments. The resulting list contains tournaments User has
     * joined or those that are not yet ended (allowing the User to join it).
     * 
     * @return The {@link List} of {@link Tournament} User participates in or
     *         the ones he can join.
     */
    @GET
    @Path("/auth/tournaments")
    public Collection<Tournament> getUserTournaments();

    /**
     * Starts a new tournament.
     * 
     * @param tournament
     *            Tournament to be started.
     * @return Nothing.
     */
    @POST
    @Path("/auth/tournaments")
    public void newTournament(Tournament tournament);

    /**
     * Enrolls User in the Tournament.
     * 
     * @param tournament
     *            Tournament to join.
     */
    @PUT
    @Path("/auth/tournaments")
    public void joinTournament(Tournament tournament);

}
