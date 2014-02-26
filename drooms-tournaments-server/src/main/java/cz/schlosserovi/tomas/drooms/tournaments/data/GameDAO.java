package cz.schlosserovi.tomas.drooms.tournaments.data;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.schlosserovi.tomas.drooms.tournaments.model.Game;
import cz.schlosserovi.tomas.drooms.tournaments.model.Playground;

@Stateless
public class GameDAO extends AbstractDAO {

    public void insertGame(Game newGame, Playground playground) {
        playground.addGame(newGame);
        newGame.setId(UUID.randomUUID());
        em.persist(newGame);
        em.flush();
    }

    public void setFinishedGame(Game game) {
        em.refresh(game);
        game.setFinished(true);
        em.merge(game);
        em.flush();
    }

    public Game getGame(UUID id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = builder.createQuery(Game.class);

        Root<Game> game = query.from(Game.class);
        query.select(game).where(builder.equal(game.get("id"), id));

        return em.createQuery(query).getSingleResult();
    }

    public List<Game> getGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = builder.createQuery(Game.class);

        query.select(query.from(Game.class));

        return em.createQuery(query).getResultList();
    }

    public List<Game> getGames(Playground playground) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = builder.createQuery(Game.class);

        Root<Game> game = query.from(Game.class);
        query.select(game).where(builder.equal(game.get("playground"), playground));

        return em.createQuery(query).getResultList();
    }

    public List<Game> getUnfinishedGames() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = builder.createQuery(Game.class);
        Root<Game> game = query.from(Game.class);

        query.select(game).where(builder.equal(game.get("finished"), false));

        return em.createQuery(query).getResultList();
    }
}
