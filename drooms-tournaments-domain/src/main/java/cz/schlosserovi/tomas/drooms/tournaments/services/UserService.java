package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;

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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {

    /**
     * Logs in already registered User on the server.
     * 
     * @param user
     *            User to be logged in.
     * @return String authentication token used in all authenticated service
     *         calls.
     */
    @POST
    @Path("/login")
    @ClientResponseType(entityType = String.class)
    public Response login(User user);

    /**
     * Logs out a User.
     * 
     * @param token
     *            Token received from <code>login</code> method.
     * @return Nothing.
     */
    @POST
    @Path("/logout")
    @ClientResponseType(entityType = String.class)
    public Response logout(@HeaderParam("Auth-Token") String token);

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
    public Response getStrategies(@HeaderParam("Auth-Token") String token);

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
    public Response newStrategy(@HeaderParam("Auth-Token") String token, Strategy strategy);

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
    public Response activateStrategy(@HeaderParam("Auth-Token") String token, Strategy strategy);

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
    public Response getPlaygrounds(@HeaderParam("Auth-Token") String token);

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
    public Response newPlayground(@HeaderParam("Auth-Token") String token, Playground playground);

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
    public Response configurePlayground(@HeaderParam("Auth-Token") String token, Playground playground);

}
