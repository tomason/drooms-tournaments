package org.drooms.tournaments.server.test;

import java.util.Collection;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.drooms.tournaments.domain.GAV;
import org.drooms.tournaments.server.data.model.GameEntity;
import org.drooms.tournaments.server.data.model.GameResultEntity;
import org.drooms.tournaments.server.data.model.PlaygroundEntity;
import org.drooms.tournaments.server.data.model.StrategyEntity;
import org.drooms.tournaments.server.data.model.TournamentEntity;
import org.drooms.tournaments.server.data.model.UserEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class ContainerTestParent {
    @Inject
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    @After
    public void cleanUpDatabase() throws Exception {
        if (em != null) {
            deleteEntities();
        }
    }

    protected TournamentEntity testTournament() {
        return testTournament(TOURNAMENT);
    }

    protected TournamentEntity testTournament(String name) {
        return em.find(TournamentEntity.class, name);
    }

    protected PlaygroundEntity testPlayground() {
        return testPlayground(PLAYGROUND);
    }

    protected PlaygroundEntity testPlayground(String name) {
        return em.find(PlaygroundEntity.class, PLAYGROUND);
    }

    protected StrategyEntity testStrategy() {
        return testStrategy(USER1_STRATEGY);
    }

    protected StrategyEntity testStrategy(GAV gav) {
        return em.find(StrategyEntity.class, gav);
    }

    protected GameEntity testGame() {
        return testGame(GAME);
    }

    protected GameEntity testGame(String id) {
        return em.find(GameEntity.class, id);
    }

    @SafeVarargs
    protected final <T> void checkResults(Collection<T> results, T... expected) {
        Assert.assertEquals("Wrong results size", expected.length, results.size());

        for (T obj : expected) {
            Assert.assertTrue("Expected result not found", results.contains(obj));
        }
    }

    protected void insertUserEntities() {
        try {
            ut.begin();

            for (String name : new String[] { USER1, USER2, USER3, ADMIN }) {
                em.persist(new UserEntity(name));
            }

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertStrategyEntities() {
        try {
            ut.begin();

            for (String name : new String[] { USER1, USER2, USER3 }) {
                StrategyEntity strategy = new StrategyEntity("org.test", name, "1.0");
                strategy.setAuthor(em.find(UserEntity.class, name));
                em.persist(strategy);
            }

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertPlaygroundEntities() {
        try {
            ut.begin();

            PlaygroundEntity playground = new PlaygroundEntity(PLAYGROUND);
            playground.setAuthor(em.find(UserEntity.class, ADMIN));
            em.persist(playground);

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertTournamentEntities() {
        try {
            ut.begin();

            TournamentEntity tournament = new TournamentEntity(TOURNAMENT);
            tournament.addPlayground(em.find(PlaygroundEntity.class, PLAYGROUND));
            em.persist(tournament);

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertTournamentResultEntities() {
        try {
            ut.begin();

            // TODO

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertGameEntities() {
        try {
            ut.begin();

            GameEntity game = new GameEntity();
            game.setId(GAME);
            game.setPlayground(em.find(PlaygroundEntity.class, PLAYGROUND));
            game.setTournament(em.find(TournamentEntity.class, TOURNAMENT));
            em.persist(game);

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void insertGameResultEntities() {
        try {
            ut.begin();

            GameEntity game = em.find(GameEntity.class, GAME);
            for (GAV gav : new GAV[] { USER1_STRATEGY, USER2_STRATEGY, USER3_STRATEGY }) {
                GameResultEntity result = new GameResultEntity();
                result.setGame(game);
                result.setStrategy(em.find(StrategyEntity.class, gav));
            }
            em.merge(game);

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteEntities() {
        try {
            ut.begin();

            em.createNativeQuery("delete from GAME_RESULT").executeUpdate();
            em.createNativeQuery("delete from PLAYGROUND_CONFIGURATION").executeUpdate();
            em.createNativeQuery("delete from TOURNAMENT_RESULT_PARTIAL").executeUpdate();

            em.createNativeQuery("delete from GAME").executeUpdate();
            em.createNativeQuery("delete from STRATEGY").executeUpdate();
            em.createNativeQuery("delete from TOURNAMENT_RESULT").executeUpdate();
            em.createNativeQuery("delete from TOURNAMENT_PLAYGROUND").executeUpdate();

            em.createNativeQuery("delete from TOURNAMENT").executeUpdate();

            em.createNativeQuery("delete from PLAYGROUND").executeUpdate();

            em.createNativeQuery("delete from PLAYER").executeUpdate();

            em.flush();

            ut.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ut.rollback();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static final String ADMIN = "admin";
    protected static final String USER1 = "tschloss";
    protected static final String USER2 = "lpetrovi";
    protected static final String USER3 = "rsynek";

    protected static final GAV USER1_STRATEGY = new GAV("org.test", USER1, "1.0");
    protected static final GAV USER2_STRATEGY = new GAV("org.test", USER2, "1.0");
    protected static final GAV USER3_STRATEGY = new GAV("org.test", USER3, "1.0");

    private static final String PLAYGROUND = "test playground";

    private static final String TOURNAMENT = "test tournament";

    private static final String GAME = "test game";
}
