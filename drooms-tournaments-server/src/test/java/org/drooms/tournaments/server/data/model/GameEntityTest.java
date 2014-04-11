package org.drooms.tournaments.server.data.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.drooms.tournaments.domain.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameEntityTest {
    private static final String ID = "test game id";

    private GameEntity entity;

    @Before
    public void createGameEntity() {
        entity = new GameEntity();
    }

    @Test
    public void testSetId() {
        entity.setId(ID);
        Assert.assertEquals("Wrong id", ID, entity.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullId() {
        entity.setId(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutableId() {
        entity.setId(ID);
        entity.setId("id2");
    }

    @Test
    public void testSetPlayground() {
        final PlaygroundEntity playground = new PlaygroundEntity();
        playground.setName("test playground name");

        entity.setPlayground(playground);
        Assert.assertEquals("Wrong playground", playground, entity.getPlayground());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullPlayground() {
        entity.setPlayground(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutablePlayground() {
        final PlaygroundEntity playground = new PlaygroundEntity();
        playground.setName("test playground name");
        entity.setPlayground(playground);

        final PlaygroundEntity playground2 = new PlaygroundEntity();
        playground2.setName("yet another playground");
        entity.setPlayground(playground2);
    }

    @Test
    public void testNewGameStatus() {
        Assert.assertEquals("Wrong status", GameStatus.NEW, entity.getStatus());
    }

    @Test
    public void testSetStatus() {
        entity.setStatus(GameStatus.IN_PROGRESS);
        Assert.assertEquals("Wrong status", GameStatus.IN_PROGRESS, entity.getStatus());

        entity.setStatus(GameStatus.FINISHED);
        Assert.assertEquals("Wrong status", GameStatus.FINISHED, entity.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullStatus() {
        entity.setStatus(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStatusBackwards1() {
        entity.setStatus(GameStatus.FINISHED);
        entity.setStatus(GameStatus.IN_PROGRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStatusBackwards2() {
        entity.setStatus(GameStatus.IN_PROGRESS);
        entity.setStatus(GameStatus.NEW);
    }

    @Test
    public void testSetLastModified() {
        final Calendar time = Calendar.getInstance();
        entity.setLastModified(time);

        Assert.assertEquals("Wrong lastModified", time, entity.getLastModified());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullLastModified() {
        entity.setLastModified(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutableLastModified() {
        entity.setLastModified(Calendar.getInstance());
        entity.setLastModified(Calendar.getInstance());
    }

    @Test
    public void testLastModifiedChangeOnStatusChange() {
        final Calendar start = Calendar.getInstance();
        start.add(Calendar.MINUTE, -5);
        entity.setLastModified(start);

        entity.setStatus(GameStatus.IN_PROGRESS);

        Assert.assertTrue("Wrong lastModified", start.compareTo(entity.getLastModified()) < 0);
    }

    @Test
    public void testArtifactPath() {
        final String path = "/home/test/artifacts";
        entity.setArtifactPath(path);

        Assert.assertEquals("Wrong path", path, entity.getArtifactPath());
    }

    @Test
    public void testSetTournament() {
        final TournamentEntity tournament = new TournamentEntity();
        tournament.setName("test tournament");
        entity.setTournament(tournament);

        Assert.assertEquals("Wrong tournament", tournament, entity.getTournament());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullTournament() {
        entity.setTournament(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutableTournament() {
        final TournamentEntity tournament = new TournamentEntity();
        tournament.setName("test tournament");
        entity.setTournament(tournament);

        entity.setTournament(new TournamentEntity());
    }

    @Test
    public void testNewGameResults() {
        Assert.assertNotNull("Null game results", entity.getGameResults());
        Assert.assertEquals("Game results not empty", 0, entity.getGameResults().size());
    }

    @Test
    public void testSetGameResults() {
        final GameResultEntity result = new GameResultEntity();
        result.setId(42L);

        entity.setGameResults(Arrays.asList(result));

        Assert.assertEquals("Wrong results size", 1, entity.getGameResults().size());
        Assert.assertEquals("Wrong result", result, entity.getGameResults().iterator().next());
    }

    @Test
    public void testAddGameResult() {
        final GameResultEntity result = new GameResultEntity();
        result.setId(42L);
        result.setGame(entity);

        entity.addGameResult(result);

        Assert.assertEquals("Wrong results size", 1, entity.getGameResults().size());
        Assert.assertEquals("Wrong result", result, entity.getGameResults().iterator().next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGameResultsIsolation() {
        Collection<GameResultEntity> gameResults = entity.getGameResults();
        gameResults.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullResults() {
        entity.setGameResults(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGameResult() {
        entity.addGameResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidGameResult1() {
        final GameResultEntity result = new GameResultEntity();
        result.setId(42L);

        entity.addGameResult(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidGameResult2() {
        entity.setId(ID);

        final GameResultEntity result = new GameResultEntity();
        result.setId(42L);
        final GameEntity other = new GameEntity();
        other.setId("other game id");
        result.setGame(other);

        entity.addGameResult(result);
    }

    @Test
    public void testConversion1() {
        entity.setId(ID);
        entity.setStatus(GameStatus.IN_PROGRESS);
        final PlaygroundEntity playground = new PlaygroundEntity();
        entity.setPlayground(playground);
        final TournamentEntity tournament = new TournamentEntity();
        entity.setTournament(tournament);

        Game domain = entity.convert(0);

        Assert.assertEquals(ID, domain.getId());
        Assert.assertFalse(domain.isFinished());
        Assert.assertNotNull(domain.getPlayground());
        Assert.assertNotNull(domain.getTournament());
        Assert.assertNull(domain.getResults());
    }

    @Test
    public void testConversion2() {
        entity.setId(ID);
        entity.setStatus(GameStatus.FINISHED);
        final PlaygroundEntity playground = new PlaygroundEntity();
        entity.setPlayground(playground);
        final TournamentEntity tournament = new TournamentEntity();
        entity.setTournament(tournament);
        final GameResultEntity result = new GameResultEntity();
        result.setGame(entity);
        entity.addGameResult(result);

        Game domain = entity.convert(1);

        Assert.assertEquals(ID, domain.getId());
        Assert.assertTrue(domain.isFinished());
        Assert.assertNotNull(domain.getPlayground());
        Assert.assertNotNull(domain.getTournament());
        Assert.assertNotNull(domain.getResults());
        Assert.assertEquals(1, domain.getResults().size());
    }

    @Test
    @SuppressWarnings("serial")
    public void testEquals() {
        final GameEntity e1 = new GameEntity();
        e1.setId(ID);
        final GameEntity e2 = new GameEntity();
        e2.setId(ID);
        final GameEntity e3 = new GameEntity();
        e3.setId("other game id");

        Assert.assertTrue(e1.equals(e1));
        Assert.assertTrue(e1.equals(e2));
        Assert.assertTrue(e2.equals(e1));
        Assert.assertTrue(new GameEntity().equals(new GameEntity()));

        Assert.assertFalse(e1.equals(e3));
        Assert.assertFalse(e1.equals(new GameEntity()));
        Assert.assertFalse(e1.equals(null));
        Assert.assertFalse(e1.equals(new GameEntity() {
            @Override
            public String getId() {
                return ID;
            }
        }));
    }
}
