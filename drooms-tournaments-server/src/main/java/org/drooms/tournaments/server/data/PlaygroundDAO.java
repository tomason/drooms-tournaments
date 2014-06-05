package org.drooms.tournaments.server.data;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.UserEntity;

@Stateless
public class PlaygroundDAO {
    private EntityManager em;

    public PlaygroundDAO() {
    }

    @Inject
    public PlaygroundDAO(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void insertPlayground(PlaygroundEntity entity) {
        em.persist(entity);
    }

    public PlaygroundEntity getPlayground(String name) {
        return em.find(PlaygroundEntity.class, name);
    }

    public void updatePlayground(PlaygroundEntity entity) {
        em.merge(entity);
    }

    public void deletePlayground(PlaygroundEntity entity) {
        em.remove(entity);
    }

    // queries
    public List<PlaygroundEntity> getPlaygrounds() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        query.select(query.from(PlaygroundEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<PlaygroundEntity> getPlaygrounds(UserEntity author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        Root<PlaygroundEntity> playground = query.from(PlaygroundEntity.class);
        query.select(playground).where(builder.equal(playground.get("author"), author));

        return em.createQuery(query).getResultList();
    }

    public List<PlaygroundEntity> getPlaygrounds(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlaygroundEntity> query = builder.createQuery(PlaygroundEntity.class);

        Root<PlaygroundEntity> playground = query.from(PlaygroundEntity.class);
        query.select(playground).where(
                builder.isMember(tournament, playground.<Collection<TournamentEntity>> get("tournaments")));

        return em.createQuery(query).getResultList();
    }

}
