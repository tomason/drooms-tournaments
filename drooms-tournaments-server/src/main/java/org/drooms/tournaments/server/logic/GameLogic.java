package org.drooms.tournaments.server.logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameFilter;
import org.drooms.tournaments.domain.GameResult;
import org.drooms.tournaments.server.data.GameDAO;
import org.drooms.tournaments.server.data.PlaygroundDAO;
import org.drooms.tournaments.server.data.StrategyDAO;
import org.drooms.tournaments.server.data.TournamentDAO;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.GameStatus;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.drooms.tournaments.server.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GameLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLogic.class);

    private UserDAO users;
    private StrategyDAO strategies;
    private PlaygroundDAO playgrounds;
    private TournamentDAO tournaments;
    private GameDAO games;
    private Path artifactStorage;

    public GameLogic() {
    }

    @Inject
    public GameLogic(UserDAO users, StrategyDAO strategies, PlaygroundDAO playgrounds, TournamentDAO tournaments,
            GameDAO games, Path artifactStorage) {
        this.users = users;
        this.strategies = strategies;
        this.playgrounds = playgrounds;
        this.tournaments = tournaments;
        this.games = games;
        this.artifactStorage = artifactStorage;
    }

    public Collection<Game> getAllGames() {
        return getConverter().convert(games.getGames());
    }

    public Collection<Game> getGames(GameFilter filter) {
        UserEntity player = null;
        StrategyEntity strategy = null;
        PlaygroundEntity playground = null;
        TournamentEntity tournament = null;
        Boolean finishedGamesOnly = null;

        if (filter != null) {
            player = filter.getPlayer() != null ? users.getUser(filter.getPlayer().getName()) : null;
            strategy = filter.getStrategy() != null ? strategies.getStrategy(filter.getStrategy().getGav()) : null;
            playground = filter.getPlayground() != null ? playgrounds.getPlayground(filter.getPlayground().getName()) : null;
            tournament = filter.getTournament() != null ? tournaments.getTournament(filter.getTournament().getName()) : null;
            finishedGamesOnly = filter.getFinished();
        }

        return getConverter().convert(games.getGames(player, strategy, playground, tournament, finishedGamesOnly));
    }

    public Game getGame(String id) {
        GameEntity entity = games.getGame(id);
        Game result = entity.convert(1);

        if (entity.getArtifactPath() != null) {
            String report = readGameReport(entity.getArtifactPath());
            if (report == null) {
                entity.setArtifactPath(null);
                games.updateGame(entity);
            } else {
                result.setGameReport(report);
            }
        }
        
        return result;
    }

    public File getGameReport(String id) {
        GameEntity entity = games.getGame(id);
        File result = null;
        if (entity.getArtifactPath() != null) {
            result = new File(entity.getArtifactPath());
            if (result.exists()) {
                return result;
            } else {
                // if the report doesn't exist, remove it from database and return null
                entity.setArtifactPath(null);
                games.updateGame(entity);

                result = null;
            }
        }

        return result;
    }

    public Collection<Game> getUserGames(String userName) {
        return getConverter().convert(games.getGames(users.getUser(userName)));
    }

    public Collection<Game> getExecutionQueue() {
        return getConverter(1).convert(games.getUnfinishedGames());
    }

    public void reserveGameExecution(Game game) {
        GameEntity entity = games.getGame(game.getId());
        entity.setStatus(GameStatus.IN_PROGRESS);

        games.updateGame(entity);
    }

    public void deliverGameResults(Game game) {
        GameEntity entity = games.getGame(game.getId());

        List<GameResultEntity> resultEntities = new LinkedList<>(entity.getGameResults());
        List<GameResult> gameResults = new LinkedList<>(game.getResults());

        if (resultEntities.size() != gameResults.size()) {
            throw new IllegalArgumentException("Wrong game results (count does not match)");
        }
        Collections.sort(resultEntities, new GameResultEntityComparator());
        Collections.sort(gameResults, new GameResultComparator());

        Iterator<GameResult> resultsIterator = gameResults.iterator();
        for (GameResultEntity resultEntity : resultEntities) {
            GameResult gameResult = resultsIterator.next();
            if (!resultEntity.getStrategy().getGav().equals(gameResult.getStrategy().getGav())) {
                throw new IllegalArgumentException("Wrong game results (GAVs do not match))");
            }
            resultEntity.setPoints(gameResult.getPoints());
        }

        Path artifacts = storeGameArtifacts(game.getId(), game.getGameReport(), game.getGameLog());
        if (artifacts != null) {
            entity.setArtifactPath(artifacts.toString());
        }
        entity.setStatus(GameStatus.FINISHED);

        games.updateGame(entity);
    }

    private Path storeGameArtifacts(String gameId, String gameReport, String gameLog) {
        Path artifactFile = artifactStorage.resolve(gameId + ".zip").toAbsolutePath();
        try (FileSystem fs = createZipFileSystem(artifactFile, true)) {
            if (gameLog != null) {
                Path gameLogFile = fs.getPath("/game.log");
                Files.write(gameLogFile, gameLog.getBytes(StandardCharsets.UTF_8));
            }
            if (gameReport != null) {
                Path gameReportFile = fs.getPath("/report.xml");
                Files.write(gameReportFile, gameReport.getBytes(StandardCharsets.UTF_8));
            }

            return artifactFile;
        } catch (IOException ex) {
            LOGGER.error("Unable to store game artifacts", ex);
        }

        return null;
    }

    private String readGameReport(String artifactPath) {
        try (FileSystem fs = createZipFileSystem(Paths.get(artifactPath), false)){
            Path gameReportFile = fs.getPath("/report.xml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Files.copy(gameReportFile, baos);

            return baos.toString(StandardCharsets.UTF_8.toString());
        } catch (IOException ex) {
            LOGGER.error("Unable to read game artifact {}",artifactPath, ex);
        }

        return null;
    }

    private FileSystem createZipFileSystem(Path artifactPath, boolean create) throws IOException {
        URI artifactUri = URI.create("jar:file:" + artifactPath.toString());

        Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }

        return FileSystems.newFileSystem(artifactUri, env);
    }

    private Converter<GameEntity, Game> getConverter() {
        return getConverter(0);
    }

    private Converter<GameEntity, Game> getConverter(int depth) {
        return Converter.forClass(GameEntity.class).setRecurseDepth(depth);
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
