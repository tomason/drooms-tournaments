package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;

public class RegistrationServiceImpl implements RegistrationService {
    @Inject
    private UserDAO users;

    @Override
    public Response register(User user) {
        ResponseBuilder builder;
        try {
            users.insertUser(user.getName(), user.getPassword());
            builder = Response.ok();
        } catch (EntityExistsException ex) {
            builder = Response.status(400).entity(ex.getMessage());
        }

        return builder.build();
    }

}
