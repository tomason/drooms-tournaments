package org.drooms.tournaments.server.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.drooms.tournaments.domain.Game;
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
        return logic.getAllGames();
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