package org.drooms.tournaments.server.services;

import java.io.File;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameFilter;
import org.drooms.tournaments.server.logic.GameLogic;
import org.drooms.tournaments.services.GameService;

public class GameServiceImpl implements GameService {
    private GameLogic logic;
    @Context
    private SecurityContext security;

    public GameServiceImpl() {
    }

    @Inject
    public GameServiceImpl(GameLogic logic) {
        this(logic, null);
    }

    public GameServiceImpl(GameLogic logic, SecurityContext security) {
        this.logic = logic;
        this.security = security;
    }

    @Override
    public Collection<Game> getGames() {
        return getGames(null);
    }

    @Override
    public Collection<Game> getGames(GameFilter filter) {
        return logic.getGames(filter);
    }

    @Override
    public Game getGame(String gameId) {
        return logic.getGame(gameId);
    }

    @Override
    public Response getGameReport(String gameId) {
        File report = logic.getGameReport(gameId);
        ResponseBuilder builder;
        if (report == null) {
            builder = Response.status(Status.GONE);
        } else {
            builder = Response.ok(report);
            builder.header("Content-Disposition", "attachment; filename=\"" + gameId + ".zip\"");
        }

        return builder.build();
    }

    @Override
    public Collection<Game> getUserGames() {
        String userName = security.getUserPrincipal().getName();
        return logic.getUserGames(userName);
    }

    @Override
    public Collection<Game> getExecutionQueue() {
        return logic.getExecutionQueue();
    }

    @Override
    public void reserveExecution(Game game) {
        logic.reserveGameExecution(game);
    }

    @Override
    public void deliverResults(Game game) {
        logic.deliverGameResults(game);
    }

}
