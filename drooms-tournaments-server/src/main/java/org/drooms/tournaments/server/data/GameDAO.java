package org.drooms.tournaments.server.data;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.GameStatus;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.drooms.tournaments.server.events.GameFinishedEvent;

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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertGame(GameEntity entity) {
        entity.setId(UUID.randomUUID().toString());

        em.persist(entity);
        em.flush();
    }

    public GameEntity getGame(String id) {
        return em.find(GameEntity.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateGame(GameEntity entity) {
        em.merge(entity);
        em.flush();

        if (entity.getStatus() == GameStatus.FINISHED) {
            finishedGames.fire(new GameFinishedEvent(entity));
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteGame(GameEntity entity) {
        em.remove(getGame(entity.getId()));
        em.flush();
    }

    // queries
    public List<GameEntity> getGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).distinct(true);

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
        return getGames(user, null, null, null, null);
    }

    public List<GameEntity> getGames(UserEntity user, StrategyEntity strategy, PlaygroundEntity playground,
            TournamentEntity tournament, Boolean finishedGames) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);

        List<Predicate> predicates = new LinkedList<Predicate>();
        if (user != null) {
            Join<GameEntity, StrategyEntity> join = game.join("gameResults").join("strategy");
            predicates.add(builder.equal(join.get("author"), user));
        }
        if (strategy != null) {
            Join<GameEntity, GameResultEntity> join = game.join("gameResults");
            predicates.add(builder.equal(join.get("strategy"), strategy));
        }
        if (playground != null) {
            predicates.add(builder.equal(game.get("playground"), playground));
        }
        if (tournament != null) {
            predicates.add(builder.equal(game.get("tournament"), tournament));
        }
        if (finishedGames != null && Boolean.TRUE.equals(finishedGames)) {
            predicates.add(builder.equal(game.get("status"), GameStatus.FINISHED));
        }

        switch (predicates.size()) {
        case 0:
            query.select(game);
            break;
        case 1:
            query.select(game).where(predicates.get(0));
            break;
        default:
            query.select(game).where(predicates.toArray(new Predicate[0]));
        }

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

        Predicate t = builder.equal(game.get("tournament"), tournament);
        Predicate p = builder.equal(game.get("playground"), playground);
        Predicate finished = builder.equal(game.get("status"), GameStatus.FINISHED);

        query.select(game).distinct(true).where(builder.and(p, t, finished));

        return em.createQuery(query).getResultList();
    }
}
