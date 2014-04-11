package org.drooms.tournaments.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.drooms.tournaments.domain.User;

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
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {

    /**
     * Registers a new User on a server.
     * 
     * @param user
     *            User to be inserted.
     */
    @POST
    @Path("/users")
    public void register(User user);

    /**
     * Chenges the password of already registered user.
     * 
     * @param user
     *            User to change password to (including the new password).
     */
    @PUT
    @Path("/auth/users")
    public void changePassword(User user);

}
