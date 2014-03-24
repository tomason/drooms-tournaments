package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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
        List<GameResult> gameResults = new LinkedList<>(game.getResults());
        Collections.sort(resultEntities, new GameResultEntityComparator());
        Collections.sort(gameResults, new GameResultComparator());

        Iterator<GameResult> resultsIterator = gameResults.iterator();
        for (GameResultEntity resultEntity : resultEntities) {
            GameResult current = resultsIterator.next();
            if (!resultEntity.getStrategy().getGav().equals(current.getStrategy().getGav())) {
                throw new IllegalArgumentException("Incomplete results were sent");
            }
            results.setPoints(resultEntity.getId(), current.getPoints());
        }

        games.setArtifacts(game.getId(), game.getGameReport(), game.getGameLog());
        games.setGameFinished(game.getId());

        return Response.ok().build();
    }

    private static class GameResultEntityComparator implements Comparator<GameResultEntity> {
        @Override
        public int compare(GameResultEntity o1, GameResultEntity o2) {
            return o1.getStrategy().getGav().compareTo(o2.getStrategy().getGav());
        }
    }

    private static class GameResultComparator implements Comparator<GameResult> {
        @Override
        public int compare(GameResult o1, GameResult o2) {
            return o1.getStrategy().getGav().compareTo(o2.getStrategy().getGav());
        }
    }
}
