package cz.schlosserovi.tomas.drooms.tournaments.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.GameResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.ExecutionResults;
import cz.schlosserovi.tomas.drooms.tournaments.domain.ExecutionSetup;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundConfigEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;

@ApplicationScoped
public class GameRegistryBean {
    @Inject
    private GameDAO gameDao;
    @Inject
    private GameResultDAO resultsDao;
    private final Map<UUID, UUID> inProgress = new HashMap<>();

    public ExecutionSetup getNewGame(UUID id) {
        GameEntity freeGame = null;
        for (GameEntity game : gameDao.getUnfinishedGames()) {
            if (!inProgress.containsKey(game.getId())) {
                freeGame = game;
                break;
            }
        }

        if (freeGame != null) {
            inProgress.put(freeGame.getId(), id);
        }
        return buildGameSetup(freeGame);
    }

    public void freeGames(UUID id) {
        for (Entry<UUID, UUID> e : inProgress.entrySet()) {
            if (e.getValue().equals(id)) {
                inProgress.remove(e.getKey());
            }
        }
    }

    public void deliverResults(ExecutionResults results) {
        GameEntity game = gameDao.getGame(results.getGameId());
        for (GameResultEntity result : resultsDao.getGameResults(game)) {
            resultsDao.setPoints(result.getId(), Integer.parseInt(results.getResults().getProperty(result.getStrategy().getAuthor().getName(), "0")));
        }
        gameDao.setFinishedGame(game.getId());

        for (UUID key : inProgress.keySet()) {
            if (key.equals(results.getGameId())) {
                inProgress.remove(key);
            }
        }
    }

    private ExecutionSetup buildGameSetup(GameEntity g) {
        if (g == null) {
            return null;
        }

        ExecutionSetup result = new ExecutionSetup();
        result.setGameId(g.getId());
        result.setClassName("org.drooms.impl.DefaultGame");
        result.setPlayground(g.getPlayground().getSource());
        result.setPlayers(buildPlayerSet(resultsDao.getGameResults(g)));
        result.setGameConfig(buildGameConfig(g.getPlayground().getConfigurations()));

        return result;
    }

    private Properties buildPlayerSet(List<GameResultEntity> resultSet) {
        Properties players = new Properties();

        for (GameResultEntity result : resultSet) {
            StrategyEntity s = result.getStrategy();
            players.setProperty(s.getAuthor().getName(), s.getGav().toString());
        }

        return players;
    }

    private Properties buildGameConfig(Collection<PlaygroundConfigEntity> configuration) {
        Properties result = new Properties();

        for (PlaygroundConfigEntity config : configuration) {
            result.setProperty(config.getKey(), config.getValue());
        }

        return result;
    }
}
