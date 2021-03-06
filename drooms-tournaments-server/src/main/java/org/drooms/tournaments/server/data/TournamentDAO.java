package org.drooms.tournaments.server.data;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.TournamentResultEntity;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.drooms.tournaments.server.events.NewTournamentEvent;

@Stateless
public class TournamentDAO {
    private EntityManager em;
    private Event<NewTournamentEvent> newTournament;

    public TournamentDAO() {
    }

    @Inject
    public TournamentDAO(EntityManager em, Event<NewTournamentEvent> newTournament) {
        this.em = em;
        this.newTournament = newTournament;
    }

    // CRUD operations
    public void insertTournament(TournamentEntity entity) {
        em.persist(entity);

        newTournament.fire(new NewTournamentEvent(entity));
        em.flush();
    }

    public TournamentEntity getTournament(String name) {
        if (name == null || name.length() == 0) {
            return null;
        } else {
            return em.find(TournamentEntity.class, name);
        }
    }

    public TournamentEntity getTournamentDetail(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds");
        tournament.fetch("results", JoinType.LEFT);
        query.select(tournament).distinct(true).where(builder.equal(tournament.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public TournamentEntity getTournamentWithPlaygrounds(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds", JoinType.LEFT);
        query.select(tournament).distinct(true).where(builder.equal(tournament.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public TournamentEntity getTournamentWithResults(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("results", JoinType.LEFT);
        query.select(tournament).distinct(true).where(builder.equal(tournament.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public void updateTournament(TournamentEntity entity) {
        em.merge(entity);
        em.flush();
    }

    public void deleteTournament(TournamentEntity entity) {
        em.remove(entity);
        em.flush();
    }

    // queries
    public List<TournamentEntity> getTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        query.select(query.from(TournamentEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentEntity> getTournaments(UserEntity player) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        Join<TournamentEntity, TournamentResultEntity> join = tournament.join("results");
        query.select(tournament).distinct(true).where(builder.equal(join.get("player"), player));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentEntity> getRunningTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds");
        query.select(tournament)
                .distinct(true)
                .where(builder.and(builder.lessThan(tournament.<Date> get("start"), builder.currentDate()),
                        builder.greaterThan(tournament.<Date> get("end"), builder.currentDate())));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentEntity> getUnfinishedTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds");
        query.select(tournament).distinct(true).where(builder.greaterThan(tournament.<Date> get("end"), builder.currentDate()));

        return em.createQuery(query).getResultList();
    }

}
