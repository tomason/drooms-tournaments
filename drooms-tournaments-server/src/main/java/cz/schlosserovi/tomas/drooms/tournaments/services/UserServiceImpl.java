package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.spi.BadRequestException;

import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;

public class UserServiceImpl implements UserService {
    @Inject
    private UserDAO users;
    @Context
    private SecurityContext security;

    public UserServiceImpl() {
    }

    @Inject
    public UserServiceImpl(UserDAO users) {
        this(users, null);
    }

    public UserServiceImpl(UserDAO users, SecurityContext security) {
        this.users = users;
        this.security = security;
    }

    @Override
    public void register(User user) {
        try {
            users.insertUser(user.getName(), user.getPassword());
        } catch (EntityExistsException ex) {
            throw new BadRequestException("This user is already registered.");
        }
    }

    @Override
    public void changePassword(User user) {
        throw new BadRequestException("This fuctionality is not yet supported.");
    }
}
