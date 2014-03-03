package cz.schlosserovi.tomas.drooms.tournaments.services;

import static cz.schlosserovi.tomas.drooms.tournaments.ConversionUtil.entityToDomain;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.schlosserovi.tomas.drooms.tournaments.data.GameDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.GameResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Game;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

@Path("/tmp")
public class GameManagementServiceImpl implements GameManagementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameManagementServiceImpl.class);
    @Inject
    private UserDAO userDao;
    @Inject
    private PlaygroundDAO playgroundDao;
    @Inject
    private StrategyDAO strategyDao;
    @Inject
    private GameDAO games;
    @Inject
    private GameResultDAO gameResults;

    @Override
    public Response getAllPlayers() {
        LOGGER.info("Request for all players received");
        List<User> users = new LinkedList<>();
        for (UserEntity dbUser : userDao.getUsers()) {
            users.add(entityToDomain(dbUser));
        }

        return Response.ok(users).build();
    }

    @Override
    public Response getAllPlaygrounds() {
        LOGGER.info("Request for all playgrounds received");
        List<Playground> playgrounds = new LinkedList<>();

        for (PlaygroundEntity dbPlayground : playgroundDao.getPlaygrounds()) {
            playgrounds.add(entityToDomain(dbPlayground));
        }

        return Response.ok(playgrounds).build();
    }

    @Override
    public Response getActiveStrategy(String userName) {
        LOGGER.info("Request for players active strategy received");
        StrategyEntity entity = strategyDao.getDefaultStrategy(userDao.getUser(userName));
        return Response.ok(entityToDomain(entity)).build();
    }

    @Override
    public Response newGame(Game game) {
        LOGGER.info("Request for new game received");
        GameEntity dbGame = games.insertGame(game.getPlayground().getName());
        for (Strategy s : game.getPlayers()) {
            gameResults.insertGameResult(dbGame.getId(), s.getGav());
        }

        return Response.ok().build();
    }

}
