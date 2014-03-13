package cz.schlosserovi.tomas.drooms.tournaments.services;

import static cz.schlosserovi.tomas.drooms.tournaments.util.ConversionUtil.entityToDomain;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;

@Path("/users")
@ApplicationScoped
public class UserServiceImpl implements UserService {
    private Map<String, String> loggedInUsers = Collections.synchronizedMap(new HashMap<String, String>());

    @Inject
    private UserDAO users;
    @Inject
    private StrategyDAO strategies;
    @Inject
    private PlaygroundDAO playgrounds;

    @Override
    public Response login(User user) {
        ResponseBuilder builder;
        try {
            if (users.loginUser(user.getName(), new String(user.getPassword()))) {
                String token = UUID.randomUUID().toString();
                loggedInUsers.put(token, user.getName());
                builder = Response.ok(token);
            } else {
                builder = Response.status(Status.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            builder = Response.status(400).entity(ex.getCause().getMessage());
        }

        return builder.build();
    }

    @Override
    public Response logout(String token) {
        ResponseBuilder builder;
        if (loggedInUsers.remove(token) != null) {
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED).entity("Invalid token");
        }

        return builder.build();
    }

    @Override
    public Response getStrategies(String token) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            List<Strategy> result = new LinkedList<>();
            for (StrategyEntity entity : strategies.getStrategies(users.getUser(userName))) {
                result.add(entityToDomain(entity));
            }
            builder = Response.ok(result);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response newStrategy(String token, Strategy strategy) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            strategies.insertStrategy(userName, strategy.getGav());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response activateStrategy(String token, Strategy strategy) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            strategies.setDefaultStrategy(strategy.getGav());
            builder = Response.status(Status.ACCEPTED);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response getPlaygrounds(String token) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            List<Playground> result = new LinkedList<>();
            for (PlaygroundEntity entity : playgrounds.getPlaygrounds(users.getUser(userName))) {
                result.add(entityToDomain(entity));
            }
            builder = Response.ok(result);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response newPlayground(String token, Playground playground) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            playgrounds.insertPlayground(userName, playground.getName(), playground.getSource());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response configurePlayground(String token, Playground playground) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            playgrounds.setPlaygroundConfiguration(playground.getName(), playground.getConfiguration());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

}
