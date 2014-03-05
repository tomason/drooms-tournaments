package cz.schlosserovi.tomas.drooms.tournaments.services;

import static cz.schlosserovi.tomas.drooms.tournaments.util.ConversionUtil.entityToDomain;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
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
import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
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
    public Response register(String userName, byte[] password) {
        ResponseBuilder builder;
        try {
            users.insertUser(userName, password);
            builder = Response.ok();
        } catch (EntityExistsException ex) {
            builder = Response.status(400).entity(ex.getMessage());
        }

        return builder.build();
    }

    @Override
    public Response login(String userName, byte[] password) {
        ResponseBuilder builder;
        try {
            if (users.loginUser(userName, password)) {
                String token = UUID.randomUUID().toString();
                loggedInUsers.put(token, userName);
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
    public Response logout(String userName) {
        ResponseBuilder builder;
        if (userName == null || userName.length() == 0) {
            builder = Response.status(Status.BAD_REQUEST).entity("User mus not be null nor empty");
        } else {
            
            for (Entry<String, String> entry : loggedInUsers.entrySet()) {
                if (entry.getValue().equals(userName)) {
                    loggedInUsers.remove(entry.getKey());
                }
            }
            builder = Response.ok();
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
    public Response newStrategy(String token, GAV gav) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            strategies.insertStrategy(userName, gav);
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response activateStrategy(String token, GAV gav) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            strategies.setDefaultStrategy(gav);
            builder = Response.ok();
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
    public Response insertOrUpdatePlayground(String token, String playgroundName, String source) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            PlaygroundEntity playground = playgrounds.getPlayground(playgroundName);
            if (playground != null) {
                playgrounds.setPlaygroundSource(playgroundName, source);
            } else {
                playgrounds.insertPlayground(userName, playgroundName, source);
            }
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response configurePlayground(String token, String playgroundName, Properties config) {
        ResponseBuilder builder;
        String userName = loggedInUsers.get(token);

        if (userName != null) {
            playgrounds.setPlaygroundConfiguration(playgroundName, config);
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

}
