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
import cz.schlosserovi.tomas.drooms.tournaments.domain.GameResults;
import cz.schlosserovi.tomas.drooms.tournaments.domain.GameSetup;
import cz.schlosserovi.tomas.drooms.tournaments.model.Game;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResult;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundConfig;
import cz.schlosserovi.tomas.drooms.tournaments.model.Strategy;

@ApplicationScoped
public class GameRegistryBean {
    @Inject
    private GameDAO gameDao;
    @Inject
    private GameResultDAO resultsDao;
    private final Map<UUID, UUID> inProgress = new HashMap<>();

    public GameSetup getNewGame(UUID id) {
        Game freeGame = null;
        for (Game game : gameDao.getUnfinishedGames()) {
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

    public void deliverResults(GameResults results) {
        Game game = gameDao.getGame(results.getGameId());
        for (GameResult result : resultsDao.getGameResults(game)) {
            result.setPoints(Integer.parseInt(results.getResults().getProperty(result.getStrategy().getAuthor().getName(), "0")));
            resultsDao.updateGameResult(result);
        }

        for (UUID key : inProgress.keySet()) {
            if (key.equals(results.getGameId())) {
                inProgress.remove(key);
            }
        }
    }

    private GameSetup buildGameSetup(Game g) {
        if (g == null) {
            return null;
        }

        GameSetup result = new GameSetup();
        result.setGameId(g.getId());
        result.setClassName("org.drooms.impl.DefaultGame");
        result.setPlayground(g.getPlayground().getSource());
        result.setPlayers(buildPlayerSet(resultsDao.getGameResults(g)));
        result.setGameConfig(buildGameConfig(g.getPlayground().getConfigurations()));

        return result;
    }

    private Properties buildPlayerSet(List<GameResult> resultSet) {
        Properties players = new Properties();

        for (GameResult result : resultSet) {
            Strategy s = result.getStrategy();
            players.setProperty(s.getAuthor().getName(),
                    String.format("%s:%s:%s", s.getGroupId(), s.getArtifactId(), s.getVersion()));
        }

        return players;
    }

    private Properties buildGameConfig(Collection<PlaygroundConfig> configuration) {
        Properties result = new Properties();

        for (PlaygroundConfig config : configuration) {
            result.setProperty(config.getKey(), config.getValue());
        }

        return result;
    }
}
