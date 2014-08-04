package org.drooms.tournaments.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameFilter;

/**
 * This service provides access to Games on the server.
 * <p>
 * There is a set of methods intended for execution client use. The execution
 * process is expected as follows:
 * <ul>
 * <li>Client calls <code>getExecutionQueue</code> method to retrieve games for
 * execution</li>
 * <li>Client picks one game.</li>
 * <li>Client calls <code>reserveExecution</code> to reserve the game execution.
 * This prevents other clients from picking the same game.</li>
 * <li>Client executes the game.</li>
 * <li>Client sets the points to the game object he picked earlier.</li>
 * <li>Client calls <code>deliverResults</code> using the same object.</li>
 * </ul>
 */
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameService {

    /**
     * Retrieves all the games on the server.
     * 
     * @return Collection of games on the server.
     */
    @GET
    @Path("/games")
    public Collection<Game> getGames();

    /**
     * Retrieves all the games based on supplied parameters.
     * 
     * @param playerName
     *            Player to retrieve games for or null.
     * @param strategyGav
     *            Strategy to retrieve games for or null.
     * @param playgroundName
     *            Playground to retrieve games for or null.
     * @param tournamentName
     *            Tournament to retrieve games for or null.
     * @return Collection of games.
     */
    @POST
    @Path("/games")
    public Collection<Game> getGames(GameFilter filter);

    /**
     * Retrieves game with details (i.e. game results, reports).
     * 
     * @param gameId
     *            Id of a game to retrieve.
     * @return Game with given id.
     */
    @GET
    @Path("/games/game")
    public Game getGame(@QueryParam("id") String gameId);

    /**
     * Retrieves game report for given game. Game report is a zip file
     * containing xml game report and execution log.
     * 
     * @param gameId
     *            Id of a game to retrieve report for.
     * @return Zip file.
     */
    @GET
    @Path("/games/{id}/report")
    @Produces("application/zip")
    public Response getGameReport(@PathParam("id") String gameId);

    /**
     * Retrieves the games of the user. The user is identified by his username
     * passed to authentication.
     * 
     * @return Collection of games user participated in.
     */
    @GET
    @Path("/auth/games")
    public Collection<Game> getUserGames();

    /**
     * Retrieves the games that have not yet been executed. This method should
     * be used by execution client to retrieve the available games.
     * 
     * @return Collection of games scheduled for execution.
     */
    @GET
    @Path("/auth/games/execution")
    public Collection<Game> getExecutionQueue();

    /**
     * Reserves the game for execution removing it from queue. If the client
     * fails to deliver result for some time, the game is returned to execution
     * queue.
     * 
     * @param game
     *            Game chosen for execution.
     */
    @PUT
    @Path("/auth/games/execution")
    public void reserveExecution(Game game);

    /**
     * Deliver results of game execution. Use the same object used for game
     * construction.
     * 
     * @param game
     *            Game with points set to its GameResult collection.
     */
    @POST
    @Path("/auth/games/execution")
    public void deliverResults(Game game);

}
