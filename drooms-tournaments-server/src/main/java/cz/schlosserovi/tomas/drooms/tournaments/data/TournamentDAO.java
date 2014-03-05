package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

public class TournamentDAO extends AbstractDAO {
    @Inject
    private PlaygroundDAO playgrounds;

    public TournamentEntity insertTournament(String name, Date start, Date end, int period, Collection<String> playgroundNames) {
        TournamentEntity result = new TournamentEntity(name, start, end, period);
        for (String playgroudName : playgroundNames) {
            result.addPlayground(playgrounds.getPlayground(playgroudName));
        }

        em.persist(result);
        em.flush();

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

    public List<TournamentEntity> getRunningTournaments() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = builder.createQuery(TournamentEntity.class);

        Root<TournamentEntity> tournament = query.from(TournamentEntity.class);
        query.select(tournament).where(
                builder.and(builder.lessThan(tournament.<Date> get("start"), builder.currentDate()),
                        builder.greaterThan(tournament.<Date> get("end"), builder.currentDate())));

        return em.createQuery(query).getResultList();
    }
}
