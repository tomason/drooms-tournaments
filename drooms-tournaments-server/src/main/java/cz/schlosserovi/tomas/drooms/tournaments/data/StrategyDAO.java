package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Stateless
public class StrategyDAO extends AbstractDAO {
    @Inject
    private UserDAO users;

    public StrategyEntity insertStrategy(String user, GAV gav) {
        return insertStrategy(user, gav, false);
    }

    public StrategyEntity insertStrategy(String userName, GAV gav, boolean active) {
        UserEntity author = users.getUser(userName);
        StrategyEntity strategy = new StrategyEntity();
        strategy.setGav(gav);
        strategy.setActive(active);
        strategy.setAuthor(author);
        try {
            StrategyEntity prev = getDefaultStrategy(author);
            if (active) {
                prev.setActive(!active);
                em.merge(prev);
            }
        } catch (NoResultException ex) {
            strategy.setActive(true);
        }

        em.persist(strategy);
        em.flush();

        return strategy;
    }

    public void setDefaultStrategy(GAV gav) {
        StrategyEntity current = getStrategy(gav);
        StrategyEntity prev = getDefaultStrategy(current.getAuthor());
        prev.setActive(false);
        current.setActive(true);

        em.merge(prev);
        em.merge(current);
        em.flush();
    }

    public StrategyEntity getStrategy(GAV gav) {
        return em.find(StrategyEntity.class, gav);
    }

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

    public StrategyEntity getDefaultStrategy(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StrategyEntity> query = builder.createQuery(StrategyEntity.class);

        Root<StrategyEntity> strategy = query.from(StrategyEntity.class);
        query.select(strategy).where(
                builder.and(builder.equal(strategy.get("author"), author), builder.equal(strategy.get("active"), true)));

        return em.createQuery(query).getSingleResult();
    }
}
