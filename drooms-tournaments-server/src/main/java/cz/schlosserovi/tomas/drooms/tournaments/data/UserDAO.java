package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import cz.schlosserovi.tomas.drooms.tournaments.model.User;

@Stateless
public class UserDAO extends AbstractDAO {

    public void insertUser(User newUser) {
        em.persist(newUser);
        em.flush();
    }

    public List<User> getUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);

        query.select(query.from(User.class));

        return em.createQuery(query).getResultList();
    }
}
