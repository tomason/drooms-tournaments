package org.drooms.tournaments.server.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.domain.GAV;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.TournamentResultEntity;
import org.drooms.tournaments.server.data.model.UserEntity;

@Stateless
public class StrategyDAO {
    private EntityManager em;

    public StrategyDAO() {
    }

    @Inject
    public StrategyDAO(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void insertStrategy(StrategyEntity entity) {
        // set active strategy if none is present
        if (getStrategies(entity.getAuthor()).size() == 0) {
            entity.setActive(true);
        }

        em.persist(entity);
    }

    public StrategyEntity getStrategy(GAV gav) {
        return em.find(StrategyEntity.class, gav);
    }

    public void updateStrategy(StrategyEntity entity) {
        em.merge(entity);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void activateStrategy(StrategyEntity entity) {
        try {
            StrategyEntity prev = getActiveStrategy(entity.getAuthor());
            prev.setActive(false);
            updateStrategy(prev);
        } catch (NoResultException ex) {
            // no previous active strategy
        }

        entity.setActive(true);
        updateStrategy(entity);
    }

    public void deleteStrategy(StrategyEntity entity) {
        em.remove(entity);
    }

    // queries
    public List<StrategyEntity> getStrategies() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StrategyEntity> query = builder.createQuery(StrategyEntity.class);

        query.select(query.from(StrategyEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<StrategyEntity> getStrategies(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StrategyEntity> query = builder.createQuery(StrategyEntity.class);

        Root<StrategyEntity> strategy = query.from(StrategyEntity.class);
        query.select(strategy).where(builder.equal(strategy.get("author"), author));

        return em.createQuery(query).getResultList();
    }

    public StrategyEntity getActiveStrategy(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StrategyEntity> query = builder.createQuery(StrategyEntity.class);

        Root<StrategyEntity> strategy = query.from(StrategyEntity.class);
        query.select(strategy).where(
                builder.and(builder.equal(strategy.get("author"), author), builder.equal(strategy.get("active"), true)));

        return em.createQuery(query).getSingleResult();
    }

    public List<StrategyEntity> getActiveStrategies(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StrategyEntity> query = builder.createQuery(StrategyEntity.class);

        Root<StrategyEntity> strategy = query.from(StrategyEntity.class);
        Join<StrategyEntity, TournamentResultEntity> join = strategy.<StrategyEntity, UserEntity> join("author").join(
                "tournamentResults");

        query.select(strategy)
                .distinct(true)
                .where(builder.and(builder.equal(join.get("tournament"), tournament),
                        builder.equal(strategy.get("active"), true)));

        return em.createQuery(query).getResultList();
    }

}
