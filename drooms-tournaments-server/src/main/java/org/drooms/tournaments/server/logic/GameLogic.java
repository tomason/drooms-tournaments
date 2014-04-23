package org.drooms.tournaments.server.logic;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameResult;
import org.drooms.tournaments.server.data.GameDAO;
import org.drooms.tournaments.server.data.UserDAO;
import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.GameStatus;
import org.drooms.tournaments.server.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class GameLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLogic.class);

    private UserDAO users;
    private GameDAO games;
    private Path artifactStorage;

    public GameLogic() {
    }

    @Inject
    public GameLogic(UserDAO users, GameDAO games, Path artifactStorage) {
        this.users = users;
        this.games = games;
        this.artifactStorage = artifactStorage;
    }

    public Collection<Game> getAllGames() {
        return getConverter().convert(games.getGames());
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
        URI artifactUri = URI.create("jar:file:" + artifactFile.toString());
        try (FileSystem fs = FileSystems.newFileSystem(artifactUri, Collections.singletonMap("create", "true"))) {
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
