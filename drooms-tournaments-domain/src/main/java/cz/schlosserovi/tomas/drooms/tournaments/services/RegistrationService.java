package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.User;

@Path("/services/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RegistrationService {

    /**
     * Registers a new User on a server.
     * 
     * @param user
     *            User to be inserted.
     * @return Nothing.
     */
    @POST
    @ClientResponseType(entityType = Void.class)
    public Response register(User user);
}
