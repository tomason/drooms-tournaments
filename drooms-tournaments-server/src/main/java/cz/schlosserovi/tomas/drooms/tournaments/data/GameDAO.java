package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameStatus;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class GameDAO extends AbstractDAO {
    @Inject
    private PlaygroundDAO playgrounds;
    @Inject
    private TournamentDAO tournaments;
    @Inject
    private StrategyDAO strategies;

    public GameEntity insertGame(String playgroundName, String tounamentName, Collection<GAV> strategies) {
        if (strategies.size() < 2) {
            throw new IllegalArgumentException("Can't play game with less than 2 players");
        }
        GameEntity result = new GameEntity();
        result.setId(UUID.randomUUID());
        result.setPlayground(playgrounds.getPlayground(playgroundName));
        result.setTournament(tournaments.getTournament(tounamentName));

        for (GAV strategy : strategies) {
            em.persist(new GameResultEntity(this.strategies.getStrategy(strategy), result));
        }

        em.persist(result);
        em.flush();

        return result;
    }

    public void setGameInProgress(UUID gameId) {
        GameEntity managed = getGame(gameId);
        managed.setStatus(GameStatus.IN_PROGRESS);

        em.merge(managed);
        em.flush();
    }

    public void setGameFinished(UUID gameId) {
        GameEntity managed = getGame(gameId);
        managed.setStatus(GameStatus.FINISHED);

        em.merge(managed);
        em.flush();
    }

    public GameEntity getGame(UUID id) {
        return em.find(GameEntity.class, id);
    }

    public GameEntity getGameWithResults(UUID id) {
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
