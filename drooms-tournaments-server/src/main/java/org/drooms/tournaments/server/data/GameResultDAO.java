package org.drooms.tournaments.server.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.UserEntity;

@Stateless
public class GameResultDAO {
    private EntityManager em;

    public GameResultDAO() {
    }

    @Inject
    public GameResultDAO(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void insertGameResult(GameResultEntity entity) {
        em.persist(entity);
        em.flush();
    }

    public GameResultEntity getGameResult(Long id) {
        return em.find(GameResultEntity.class, id);
    }

    public void updateGameResult(GameResultEntity entity) {
        em.merge(entity);
        em.flush();
    }

    public void deleteGameResult(GameResultEntity entity) {
        em.remove(entity);
        em.flush();
    }

    // queries
    public List<GameResultEntity> getGameResults() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResultEntity> query = builder.createQuery(GameResultEntity.class);

        query.select(query.from(GameResultEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<GameResultEntity> getGameResults(StrategyEntity strategy) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResultEntity> query = builder.createQuery(GameResultEntity.class);

        Root<GameResultEntity> gameResult = query.from(GameResultEntity.class);
        query.select(gameResult).where(builder.equal(gameResult.get("strategy"), strategy));

        return em.createQuery(query).getResultList();
    }

    public List<GameResultEntity> getGameResults(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResultEntity> query = builder.createQuery(GameResultEntity.class);

        Root<GameResultEntity> strategy = query.from(GameResultEntity.class);
        Join<GameResultEntity, StrategyEntity> join = strategy.join("strategy");
        query.select(strategy).where(builder.equal(join.get("author"), author));

        return em.createQuery(query).getResultList();
    }

    public List<GameResultEntity> getGameResults(PlaygroundEntity playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResultEntity> query = builder.createQuery(GameResultEntity.class);

        Root<GameResultEntity> strategy = query.from(GameResultEntity.class);
        Join<GameResultEntity, GameEntity> join = strategy.join("game");
        query.select(strategy).where(builder.equal(join.get("playground"), playground));

        return em.createQuery(query).getResultList();
    }

}
