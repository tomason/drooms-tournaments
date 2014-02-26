package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.model.User;

@Stateless
public class StrategyDAO extends AbstractDAO {

    public void insertStrategy(Strategy newStrategy, User author) {
        author.addStrategy(newStrategy);
        em.persist(newStrategy);
        em.flush();
    }

    public List<Strategy> getStrategies() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Strategy> query = builder.createQuery(Strategy.class);

        query.select(query.from(Strategy.class));

        return em.createQuery(query).getResultList();
    }

    public List<Strategy> getStrategies(User author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Strategy> query = builder.createQuery(Strategy.class);

        Root<Strategy> strategy = query.from(Strategy.class);
        query.select(strategy).where(builder.equal(strategy.get("author"), author));

        return em.createQuery(query).getResultList();
    }
}
