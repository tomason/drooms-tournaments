package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

@Stateless
public class GameDAO extends AbstractDAO {
    @Inject
    private PlaygroundDAO playgrounds;
    @Inject
    private TournamentDAO tournaments;
    @Inject
    private StrategyDAO strategies;

    public GameEntity insertGame(String playgroundName, String tounamentName, Collection<GAV> strategies) {
        if (strategies.size() < 2) {
            throw new IllegalArgumentException("Can't play game with less than 2 players");
        }
        GameEntity result = new GameEntity();
        result.setId(UUID.randomUUID());
        result.setPlayground(playgrounds.getPlayground(playgroundName));
        result.setTournament(tournaments.getTournament(tounamentName));

        for (GAV strategy : strategies) {
            em.persist(new GameResultEntity(this.strategies.getStrategy(strategy), result));
        }

        em.persist(result);
        em.flush();

        return result;
    }

    public void setFinishedGame(UUID gameId) {
        GameEntity managed = getGame(gameId);
        managed.setFinished(true);

        em.merge(managed);
        em.flush();
    }

    public GameEntity getGame(UUID id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).where(builder.equal(game.get("id"), id));

        return em.createQuery(query).getSingleResult();
    }

    public List<GameEntity> getGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        query.select(query.from(GameEntity.class));

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getGames(PlaygroundEntity playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).where(builder.equal(game.get("playground"), playground));

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getGames(TournamentEntity tournament) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        Root<GameEntity> game = query.from(GameEntity.class);
        query.select(game).where(builder.equal(game.get("tournament"), tournament));

        return em.createQuery(query).getResultList();
    }

    public List<GameEntity> getUnfinishedGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);
        Root<GameEntity> game = query.from(GameEntity.class);

        query.select(game).where(builder.equal(game.get("finished"), false));

        return em.createQuery(query).getResultList();
    }
}
