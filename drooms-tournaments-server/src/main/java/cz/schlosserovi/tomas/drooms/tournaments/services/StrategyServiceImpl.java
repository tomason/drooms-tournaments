package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

public class StrategyServiceImpl implements StrategyService {
    @Inject
    private UserDAO users;
    @Inject
    private StrategyDAO strategies;
    @Context
    private SecurityContext security;

    @Override
    public Response getStrategies(boolean onlyActive) {
        return Response.ok(Converter.forClass(StrategyEntity.class).convert(strategies.getStrategies())).build();
    }

    @Override
    public Response getUserStrategies() {
        String name = security.getUserPrincipal().getName();
        UserEntity user = users.getUser(name);

        return Response.ok(Converter.forClass(StrategyEntity.class).convert(strategies.getStrategies(user))).build();
    }

    @Override
    public Response newStrategy(Strategy strategy) {
        ResponseBuilder builder;
        String userName = security.getUserPrincipal().getName();

        try {
            strategies.insertStrategy(userName, strategy.getGav());
            builder = Response.ok();
        } catch (EntityExistsException ex) {
            builder = Response.status(Status.BAD_REQUEST).entity("Strategy with this GAV is already registered.");
        }

        return builder.build();
    }

    @Override
    public Response setActiveStrategy(Strategy strategy) {
        strategies.setDefaultStrategy(strategy.getGav());
        return Response.ok().build();
    }

}
