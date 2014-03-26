package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;

@Stateless
public class TournamentResultDAO {
    private EntityManager em;
    private UserDAO users;
    private TournamentDAO tournaments;

    public TournamentResultDAO() {
    }

    @Inject
    public TournamentResultDAO(EntityManager em, UserDAO users, TournamentDAO tournaments) {
        this.em = em;
        this.users = users;
        this.tournaments = tournaments;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public TournamentResultEntity insertResult(String userName, String tournamentName) {
        TournamentResultEntity result = new TournamentResultEntity();
        result.setPlayer(users.getUserWithTournamentResults(userName));
        result.setTournament(tournaments.getTournamentWithResults(tournamentName));

        em.persist(result);
        em.flush();

        return result;
    }

    public TournamentResultEntity getTournamentResult(Long id) {
        return em.find(TournamentResultEntity.class, id);
    }

    public void setPosition(Long id, int position) {
        if (position < 1) {
            throw new IllegalArgumentException("Invalid position");
        }
        TournamentResultEntity entity = getTournamentResult(id);
        entity.setPosition(position);

        em.merge(entity);
        em.flush();
    }

    public List<TournamentResultEntity> getResults() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TournamentResultEntity> query = builder.createQuery(TournamentResultEntity.class);

        query.select(query.from(TournamentResultEntity.class));

        return em.createQuery(query).getResultList();
    }
}
