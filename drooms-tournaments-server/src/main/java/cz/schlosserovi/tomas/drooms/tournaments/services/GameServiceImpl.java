package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Game;
import cz.schlosserovi.tomas.drooms.tournaments.logic.GameLogic;

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
    public Response getGames() {
        return Response.ok(logic.getAllGames()).build();
    }

    @Override
    public Response getUserGames() {
        String userName = security.getUserPrincipal().getName();
        return Response.ok(logic.getUserGames(userName)).build();
    }

    @Override
    public Response getExecutionQueue() {
        return Response.ok(logic.getExecutionQueue()).build();
    }

    @Override
    public Response reserveExecution(Game game) {
        logic.reserveGameExecution(game);
        return Response.ok().build();
    }

    @Override
    public Response deliverResults(Game game) {
        logic.deliverGameResults(game);
        return Response.ok().build();
    }

}
