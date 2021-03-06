package org.drooms.tournaments.server.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.UserEntity;

@Stateless
public class UserDAO {
    private EntityManager em;

    public UserDAO() {
    }

    @Inject
    public UserDAO(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void insertUser(UserEntity entity) {
        em.persist(entity);
    }

    /**
     * Searches database for user with given name.
     * 
     * @param name
     *            Name of the user to find.
     * @return UserEntity with given name or null if no such user exists.
     */
    public UserEntity getUser(String name) {
        if (name == null || name.length() == 0) {
            return null;
        } else {
            return em.find(UserEntity.class, name);
        }
    }

    public UserEntity getUserWithTournamentResults(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        Root<UserEntity> user = query.from(UserEntity.class);
        user.fetch("tournamentResults", JoinType.LEFT);
        query.select(user).where(builder.equal(user.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public UserEntity getUserWithStrategies(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        Root<UserEntity> user = query.from(UserEntity.class);
        user.fetch("strategies", JoinType.LEFT);
        query.select(user).where(builder.equal(user.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public void updateUser(UserEntity entity) {
        em.merge(entity);
    }

    public void removeUser(UserEntity entity) {
        em.remove(entity);
    }

    // queries
    public List<UserEntity> getUsers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        query.select(query.from(UserEntity.class));

        return em.createQuery(query).getResultList();
    }
}
