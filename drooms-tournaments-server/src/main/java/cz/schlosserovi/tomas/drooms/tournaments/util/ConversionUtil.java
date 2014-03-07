package cz.schlosserovi.tomas.drooms.tournaments.util;

import java.util.Properties;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GameResult;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.model.GameResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundConfigEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;

public final class ConversionUtil {
    private ConversionUtil() {
    }

    public static Strategy entityToDomain(StrategyEntity entity) {
        return new Strategy(entity.getGav(), entity.isActive());
    }

    public static Playground entityToDomain(PlaygroundEntity entity) {
        Properties p = new Properties();
        for (PlaygroundConfigEntity config : entity.getConfigurations()) {
            p.setProperty(config.getKey(), config.getValue());
        }
        return new Playground(entity.getName(), entity.getSource(), entity.getMaxPlayers(), p);
    }

    public static User entityToDomain(UserEntity entity) {
        return new User(entity.getName());
    }

    public static GameResult entityToDomain(GameResultEntity entity) {
        GameResult result = new GameResult();
        result.setGameId(entity.getGame().getId().toString());
        result.setPlaygroundName(entity.getGame().getPlayground().getName());
        result.setPoints(entity.getPoints());
        result.setFinished(entity.getGame().isFinished());

        return result;
    }

}
