package org.drooms.tournaments.server.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.TournamentResultEntity;
import org.drooms.tournaments.server.events.UpdatedTournamentEvent;

@Stateless
public class TournamentResultDAO {
    private EntityManager em;
    private Event<UpdatedTournamentEvent> updates;

    public TournamentResultDAO() {
    }

    @Inject
    public TournamentResultDAO(EntityManager em, Event<UpdatedTournamentEvent> updates) {
        this.em = em;
        this.updates = updates;
    }

    // CRUD operations
    public void insertTournamentResult(TournamentResultEntity entity) {
        em.persist(entity);
    }

    public TournamentResultEntity getTournamentResult(Long id) {
        return em.find(TournamentResultEntity.class, id);
    }

    public void updateTournamentResult(TournamentResultEntity entity) {
        em.merge(entity);
    }

    public void updateTournamentresults(Collection<TournamentResultEntity> entities) {
        updateTournamentResults(entities, false);
    }

    public void updateTournamentResults(Collection<TournamentResultEntity> entities, boolean notify) {
        Collection<TournamentEntity> tournaments = new HashSet<>();
        for (TournamentResultEntity entity : entities) {
            em.merge(entity);
            tournaments.add(entity.getTournament());
        }
        em.flush();

        if (notify) {
            for (TournamentEntity tournament : tournaments) {
                updates.fire(new UpdatedTournamentEvent(tournament));
            }
        }
    }

    public void removeTournamentResult(TournamentResultEntity entity) {
        em.remove(entity);
    }

    // queries
    public List<TournamentResultEntity> getTournamentResults() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentResultEntity> query = builder.createQuery(TournamentResultEntity.class);

        query.select(query.from(TournamentResultEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<TournamentResultEntity> getTournamentResults(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentResultEntity> query = builder.createQuery(TournamentResultEntity.class);

        Root<TournamentResultEntity> tournamentResult = query.from(TournamentResultEntity.class);
        query.select(tournamentResult).where(builder.equal(tournamentResult.get("tournament"), tournament));

        return em.createQuery(query).getResultList();
    }
}
