package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameStatus;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class GameDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameDAO.class);
    private static final Path artifactStorage;

    private EntityManager em;
    private PlaygroundDAO playgrounds;
    private TournamentDAO tournaments;
    private StrategyDAO strategies;

    static {
        String dataDir = System.getenv("OPENSHIFT_DATA_DIR");
        if (dataDir == null) {
            dataDir = System.getProperty("user.home");
        }
        artifactStorage = Paths.get(dataDir, "artifacts");
        if (!Files.exists(artifactStorage)) {
            try {
                Files.createDirectory(artifactStorage);
            } catch (IOException ex) {
                LOGGER.error("Unable to create artifact storage", ex);
            }
        }
    }

    public GameDAO() {
    }

    @Inject
    public GameDAO(EntityManager em, PlaygroundDAO playgrounds, StrategyDAO strategies, TournamentDAO tournaments) {
        this.em = em;
        this.playgrounds = playgrounds;
        this.tournaments = tournaments;
    }
    
    public GameEntity insertGame(String playgroundName, String tounamentName, Collection<GAV> strategies) {
        if (strategies.size() < 2) {
            throw new IllegalArgumentException("Can't play game with less than 2 players");
        }
        GameEntity result = new GameEntity();
        result.setId(UUID.randomUUID().toString());
        result.setPlayground(playgrounds.getPlayground(playgroundName));
        result.setTournament(tournaments.getTournament(tounamentName));

        for (GAV strategy : strategies) {
            em.persist(new GameResultEntity(this.strategies.getStrategy(strategy), result));
        }

        em.persist(result);
        em.flush();

        return result;
    }

    public void setGameInProgress(String gameId) {
        GameEntity managed = getGame(gameId);
        managed.setStatus(GameStatus.IN_PROGRESS);

        em.merge(managed);
        em.flush();
    }

    public void setGameFinished(String gameId) {
        GameEntity managed = getGame(gameId);
        managed.setStatus(GameStatus.FINISHED);

        em.merge(managed);
        em.flush();
    }

    public void setArtifacts(String gameId, String gameReport, String gameLog) {
        if (gameReport == null && gameLog == null) {
            // no logs
            return;
        }
        GameEntity managed = getGame(gameId);
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
        } catch (IOException ex) {
            LOGGER.error("Unable to store game artifacts", ex);
        }
        managed.setArtifactPath(artifactFile.toString());

        em.merge(managed);
        em.flush();
    }

    public GameEntity getGame(String id) {
        return em.find(GameEntity.class, id);
    }

    public GameEntity getGameWithResults(String id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        game.fetch("gameResults");
        query.select(game).where(builder.equal(game.get("id"), id));

        return em.createQuery(query).getSingleResult();
    }

    public List<GameEntity> getGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        game.fetch("gameResults");
        query.select(game);

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getGames(PlaygroundEntity playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).where(builder.equal(game.get("playground"), playground));

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getGames(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).where(builder.equal(game.get("tournament"), tournament));

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getGames(UserEntity user) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        Join<GameEntity, StrategyEntity> join = game.join("gameResults").join("strategy");
        query.select(game).where(builder.equal(join.get("author"), user));

        return em.createQuery(query).getResultList();
    }

    /**
     * Collects list of games that are either in NEW state or are in IN_PROGRESS
     * state for more than two hours.
     * 
     * @return List of unfinished games.
     */
    public List<GameEntity> getUnfinishedGames() {
        Calendar twoHoursAgo = Calendar.getInstance();
        twoHoursAgo.add(Calendar.HOUR_OF_DAY, -2);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        game.fetch("gameResults");

        Predicate newGame = builder.equal(game.get("status"), GameStatus.NEW);
        Predicate inProgress = builder.and(builder.equal(game.get("status"), GameStatus.IN_PROGRESS),
                builder.lessThan(game.<Calendar> get("lastModified"), twoHoursAgo));
        query.select(game).where(builder.or(newGame, inProgress));

        return em.createQuery(query).getResultList();
    }
}
