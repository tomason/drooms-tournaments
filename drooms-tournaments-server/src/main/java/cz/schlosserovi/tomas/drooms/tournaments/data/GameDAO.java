package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.events.GameFinishedEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameStatus;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class GameDAO {
    private EntityManager em;
    private Event<GameFinishedEvent> finishedGames;

    public GameDAO() {
    }

    @Inject
    public GameDAO(EntityManager em, Event<GameFinishedEvent> finishedGames) {
        this.em = em;
        this.finishedGames = finishedGames;
    }

    // CRUD operations
    public void insertGame(GameEntity entity) {
        entity.setId(UUID.randomUUID().toString());

        em.persist(entity);
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

    public void updateGame(GameEntity entity) {
        em.merge(entity);

        if (entity.getStatus() == GameStatus.FINISHED) {
            finishedGames.fire(new GameFinishedEvent(entity));
        }
    }

    public void deleteGame(GameEntity entity) {
        em.remove(entity);
    }

    // queries
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

    public List<GameEntity> getGamesWithResults(TournamentEntity tournament, PlaygroundEntity playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        game.fetch("gameResults");

        Predicate t = builder.equal(game.get("tournament"), tournament);
        Predicate p = builder.equal(game.get("playground"), playground);
        Predicate finished = builder.equal(game.get("status"), GameStatus.FINISHED);

        query.select(game).distinct(true).where(builder.and(p, t, finished));

        return em.createQuery(query).getResultList();
    }
}
