package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.events.NewTournamentEvent;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

public class TournamentDAO extends AbstractDAO {
    @Inject
    private PlaygroundDAO playgrounds;
    @Inject
    private Event<NewTournamentEvent> newTournament;

    public TournamentEntity insertTournament(String name, Calendar start, Calendar end, int period, Collection<String> playgroundNames) {
        TournamentEntity result = new TournamentEntity(name, start, end, period);
        for (String playgroudName : playgroundNames) {
            result.addPlayground(playgrounds.getPlayground(playgroudName));
        }

        em.persist(result);
        em.flush();

        newTournament.fire(new NewTournamentEvent(result));

        return result;
    }

    public TournamentEntity getTournament(String name) {
        return em.find(TournamentEntity.class, name);
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
        Join<TournamentEntity, TournamentResultEntity> join = tournament.join("results");
        query.select(tournament).where(builder.equal(join.get("player"), player));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentEntity> getRunningTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        query.select(tournament).where(
                builder.and(builder.lessThan(tournament.<Date> get("start"), builder.currentDate()),
                        builder.greaterThan(tournament.<Date> get("end"), builder.currentDate())));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentEntity> getUnfinishedTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        query.select(tournament).where(builder.greaterThan(tournament.<Date> get("end"), builder.currentDate()));

        return em.createQuery(query).getResultList();
    }
}
