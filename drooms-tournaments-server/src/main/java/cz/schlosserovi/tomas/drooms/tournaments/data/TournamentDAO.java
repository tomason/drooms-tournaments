package cz.schlosserovi.tomas.drooms.tournaments.data;

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

import cz.schlosserovi.tomas.drooms.tournaments.events.NewTournamentEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

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

    public void insertTournament(TournamentEntity tournament) {
        em.persist(tournament);

        newTournament.fire(new NewTournamentEvent(tournament));
    }

    public void updateTournament(TournamentEntity tournament) {
        em.merge(tournament);
    }

    public void deleteTournament(TournamentEntity tournament) {
        em.remove(tournament);
    }

    public TournamentEntity getTournament(String name) {
        return em.find(TournamentEntity.class, name);
    }

    public TournamentEntity getTournamentWithPlaygrounds(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds", JoinType.LEFT);
        query.select(tournament).distinct(true).where(builder.equal(tournament.get("name"), name));

        return em.createQuery(query).getSingleResult();
    }

    public TournamentEntity getTournamentWithPlaygroundsAndGames(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        tournament.fetch("playgrounds", JoinType.LEFT);
        tournament.fetch("games", JoinType.LEFT);
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
        tournament.fetch("playgrounds");
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
        tournament.fetch("games", JoinType.LEFT);
        query.select(tournament).distinct(true).where(builder.greaterThan(tournament.<Date> get("end"), builder.currentDate()));

        return em.createQuery(query).getResultList();
    }
}
