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

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

// FIXME add ScoreBoard service
/**
 * Provides User methods to manipulate his profile on the server. </p> The
 * current model is as follows:
 * <ul>
 * <li>User is identified by his name.</li>
 * <li>User can submit his own Playground. Playgrounds are identified by their
 * names.</li>
 * <li>User can register multiple Strategies, but only one is active at any
 * given time.</li>
 * <li>The Strategies must have unique GAV. Two users can't register strategy
 * with same GAV.</li>
 * <li>Strategies must be deployed in a public repository. Drooms nexus is
 * preferred, but Maven Central or JBoss Public will work too.</li>
 * <li>User can start a new Tournament. To do so he must specify start date, end
 * date and period in hours.</li>
 * <li>Any User with at least one strategy can enroll in Tournament. The active
 * Strategy can change during the Tournament.</li>
 * <li>The server automatically schedules Games from {@code startDate} date
 * every {@code period} hours with current active Strategies.</li>
 * </ul>
 */
@Path("/services/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {

    /**
     * Lists all the User's strategies.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @return {@link List} of {@link Strategy} registered for this User.
     */
    @GET
    @Path("/strategies")
    @ClientResponseType(entityType = List.class)
    public Response getStrategies();

    /**
     * Registers a new Strategy for User.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param strategy
     *            Strategy to be registered.
     * @return Nothing.
     */
    @POST
    @Path("/strategies")
    @ClientResponseType(entityType = Void.class)
    public Response newStrategy(Strategy strategy);

    /**
     * Sets the specified Strategy as User's active one.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param strategy
     *            Strategy to be set active.
     * @return Nothing.
     */
    @PUT
    @Path("/strategies")
    @ClientResponseType(entityType = Void.class)
    public Response activateStrategy(Strategy strategy);

    /**
     * List all the User's Playgrounds.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @return {@link List} of {@link Playground} registered for this User.
     */
    @GET
    @Path("/playgrounds")
    @ClientResponseType(entityType = List.class)
    public Response getPlaygrounds();

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
    @Path("/playgrounds")
    @ClientResponseType(entityType = Void.class)
    public Response newPlayground(Playground playground);

    /**
     * Sets the Playground's configuration.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @param playground
     *            Playground containing configuration to be used.
     * @return Nothing.
     */
    @PUT
    @Path("/playgrounds")
    @ClientResponseType(entityType = Void.class)
    public Response configurePlayground(Playground playground);

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
    @Path("/tournaments")
    @ClientResponseType(entityType = List.class)
    public Response getTournaments();

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
    @Path("/tournaments")
    @ClientResponseType(entityType = Void.class)
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
    @Path("/tournaments")
    @ClientResponseType(entityType = Void.class)
    public Response joinTournament(Tournament tournament);

}
