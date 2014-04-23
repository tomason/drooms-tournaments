package org.drooms.tournaments.server.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.drooms.tournaments.domain.GAV;
import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.GameStatus;
import org.drooms.tournaments.server.events.GameFinishedEvent;
import org.drooms.tournaments.server.test.ContainerTestParent;
import org.drooms.tournaments.server.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameDAOTest extends ContainerTestParent {
    @Inject
    private GameDAO games;
    private GameEntity entity;

    @Deployment
    public static WebArchive getDeployment() throws IOException {
        return Deployments.defaultDeployment();
    }

    @Before
    public void init() {
        insertUserEntities();
        insertStrategyEntities();
        insertPlaygroundEntities();
        insertTournamentEntities();

        entity = setupGame();
    }

    @Test
    public void testInsertGame() {
        games.insertGame(entity);

        Assert.assertNotNull(entity.getId());
        checkResults(games.getGames(), entity);
    }

    @Test
    public void testGetGame() {
        games.insertGame(entity);
        Assert.assertNotNull(entity.getId());

        Assert.assertNotNull(games.getGame(entity.getId()));
        Assert.assertEquals(entity, games.getGame(entity.getId()));
    }

    @Test
    public void testGetNonExistentGame() {
        Assert.assertNull(games.getGame("arbitrary non existent id"));
    }

    @Test
    public void testUpdateGame() {
        games.insertGame(entity);

        entity.setStatus(GameStatus.IN_PROGRESS);
        games.updateGame(entity);

        checkResults(games.getGames(), entity);
        Assert.assertEquals("Event triggered", 0, EventObserver.getEvents().size());
        Assert.assertEquals(GameStatus.IN_PROGRESS, games.getGame(entity.getId()).getStatus());

        entity.setStatus(GameStatus.FINISHED);
        games.updateGame(entity);
        Assert.assertEquals("Event not triggered", 1, EventObserver.getEvents().size());
        Assert.assertEquals(GameStatus.FINISHED, games.getGame(entity.getId()).getStatus());
    }

    @Test
    public void testDeleteGame() {
        games.insertGame(entity);
        checkResults(games.getGames(), entity);

        games.deleteGame(entity);
        checkResults(games.getGames());
    }

    @Test
    public void testGetGames() {
        checkResults(games.getGames());

        games.insertGame(entity);

        checkResults(games.getGames(), entity);
    }

    @Test
    public void testGetGamesByPlayground() {
        checkResults(games.getGames(testPlayground()));

        games.insertGame(entity);

        checkResults(games.getGames(testPlayground()), entity);
    }

    private GameEntity setupGame() {
        GameEntity result = new GameEntity();

        result.setTournament(testTournament());
        result.setPlayground(testPlayground());
        setGameResults(result);

        return result;
    }

    private void setGameResults(GameEntity entity) {
        for (GAV gav : new GAV[] { USER1_STRATEGY, USER2_STRATEGY, USER3_STRATEGY }) {
            GameResultEntity gre = new GameResultEntity();

            gre.setGame(entity);
            gre.setStrategy(testStrategy(gav));
            entity.addGameResult(gre);
        }
    }

    public static class EventObserver {
        private static List<GameFinishedEvent> events = new LinkedList<>();

        public static void observeEvents(@Observes(during = TransactionPhase.AFTER_SUCCESS) GameFinishedEvent event) {
            events.add(event);
        }

        public static List<GameFinishedEvent> getEvents() {
            return events;
        }

    }
}
