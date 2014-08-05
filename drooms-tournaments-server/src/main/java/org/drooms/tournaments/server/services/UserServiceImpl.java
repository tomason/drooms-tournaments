package org.drooms.tournaments.server.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.server.logic.UserLogic;
import org.drooms.tournaments.services.UserService;

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

    @Override
    public Collection<User> getUsers() {
        return logic.getUsers();
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

    @Override
    public User getUser(String userName) {
        return logic.getUser(userName);
    }
}
