package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
@Path("/services/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {

    /**
     * Registers a new User on a server.
     * 
     * @param user
     *            User to be inserted.
     * @return Nothing.
     */
    @POST
    public Response register(User user);

    @PUT
    @Path("/auth")
    public Response changePassword(User user);

}
