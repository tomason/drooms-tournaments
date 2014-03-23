package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.GameResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Game;
import cz.schlosserovi.tomas.drooms.tournaments.domain.GameResult;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

public class GameServiceImpl implements GameService {
    @Inject
    private UserDAO users;
    @Inject
    private GameDAO games;
    @Inject
    private GameResultDAO results;
    @Context
    private SecurityContext security;

    @Override
    public Response getGames() {
        return Response.ok(Converter.forClass(GameEntity.class).convert(games.getGames())).build();
    }

    @Override
    public Response getUserGames() {
        UserEntity user = users.getUser(security.getUserPrincipal().getName());
        return Response.ok(Converter.forClass(GameEntity.class).convert(games.getGames(user))).build();
    }

    @Override
    public Response getExecutionQueue() {
        List<Game> result = Converter.forClass(GameEntity.class).setRecurseDepth(1).convert(games.getUnfinishedGames());
        return Response.ok(result).build();
    }

    @Override
    public Response reserveExecution(Game game) {
        games.setGameInProgress(game.getId());
        return Response.ok().build();
    }

    @Override
    public Response deliverResults(Game game) {
        List<GameResultEntity> resultEntities = results.getGameResults(games.getGame(game.getId()));
        // There has to be a better way
        for (GameResultEntity resultEntity : resultEntities) {
            for (GameResult gameResult : game.getResults()) {
                if (resultEntity.getStrategy().getGav().equals(gameResult.getStrategy().getGav())) {
                    results.setPoints(resultEntity.getId(), gameResult.getPoints());
                    break;
                }
            }
        }
        games.setArtifacts(game.getId(), game.getGameReport(), game.getGameLog());
        games.setGameFinished(game.getId());

        return Response.ok().build();
    }

}
