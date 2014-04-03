package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.logic.UserLogic;

public class UserServiceImpl implements UserService {
    private UserLogic logic;
    @Context
    private SecurityContext security;

    public UserServiceImpl() {
    }

    @Inject
    public UserServiceImpl(UserLogic logic) {
        this(logic, null);
    }

    public UserServiceImpl(UserLogic logic, SecurityContext security) {
        this.logic = logic;
        this.security = security;
    }

    @Override
    public void register(User user) {
        logic.registerUser(user);
    }

    @Override
    public void changePassword(User user) {
        logic.changePassword(security.getUserPrincipal().getName(), user);
    }
}
