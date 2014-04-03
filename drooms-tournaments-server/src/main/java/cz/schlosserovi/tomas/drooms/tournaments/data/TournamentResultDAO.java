package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentResultEntity;

@Stateless
public class TournamentResultDAO {
    private EntityManager em;

    public TournamentResultDAO() {
    }

    @Inject
    public TournamentResultDAO(EntityManager em) {
        this.em = em;
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
}
