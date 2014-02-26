package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.model.User;

@Stateless
public class PlaygroundDAO extends AbstractDAO {

    public void insertPlayground(Playground newPlayground, User author) {
        author.addPlayground(newPlayground);
        em.persist(newPlayground);
        em.flush();
    }

    public void updatePlayground(Playground updatedPlayground) {
        em.merge(updatedPlayground);
        em.flush();
    }

    public List<Playground> getPlaygrounds() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Playground> query = builder.createQuery(Playground.class);

        query.select(query.from(Playground.class));

        return em.createQuery(query).getResultList();
    }

    public List<Playground> getPlaygrounds(User author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Playground> query = builder.createQuery(Playground.class);

        Root<Playground> gameResult = query.from(Playground.class);
        query.select(gameResult).where(builder.equal(gameResult.get("author"), author));

        return em.createQuery(query).getResultList();
    }
}
