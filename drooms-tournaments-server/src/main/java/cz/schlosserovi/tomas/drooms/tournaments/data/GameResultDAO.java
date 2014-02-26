package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.Game;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResult;
import cz.schlosserovi.tomas.drooms.tournaments.model.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.model.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.model.User;

@Stateless
public class GameResultDAO extends AbstractDAO {

    public void insertGameResult(GameResult newGameResult, Strategy strategy, Game game) {
        strategy.addGameResult(newGameResult);
        game.addGameResult(newGameResult);
        em.persist(newGameResult);
        em.flush();
    }

    public void updateGameResult(GameResult updatedGameResult) {
        em.merge(updatedGameResult);
        em.flush();
    }

    public List<GameResult> getGameResults() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResult> query = builder.createQuery(GameResult.class);

        query.select(query.from(GameResult.class));

        return em.createQuery(query).getResultList();
    }

    public List<GameResult> getGameResults(Game game) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResult> query = builder.createQuery(GameResult.class);

        Root<GameResult> gameResult = query.from(GameResult.class);
        query.select(gameResult).where(builder.equal(gameResult.get("game"), game));

        return em.createQuery(query).getResultList();
    }

    public List<GameResult> getGameResults(Strategy strategy) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResult> query = builder.createQuery(GameResult.class);

        Root<GameResult> gameResult = query.from(GameResult.class);
        query.select(gameResult).where(builder.equal(gameResult.get("strategy"), strategy));

        return em.createQuery(query).getResultList();
    }

    public List<GameResult> getGameResults(User author) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResult> query = builder.createQuery(GameResult.class);

        Root<GameResult> strategy = query.from(GameResult.class);
        Join<GameResult, Strategy> join = strategy.join("strategy");
        query.select(strategy).where(builder.equal(join.get("author"), author));

        return em.createQuery(query).getResultList();
    }

    public List<GameResult> getGameResults(Playground playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GameResult> query = builder.createQuery(GameResult.class);

        Root<GameResult> strategy = query.from(GameResult.class);
        Join<GameResult, Game> join = strategy.join("game");
        query.select(strategy).where(builder.equal(join.get("playground"), playground));

        return em.createQuery(query).getResultList();
    }
}
