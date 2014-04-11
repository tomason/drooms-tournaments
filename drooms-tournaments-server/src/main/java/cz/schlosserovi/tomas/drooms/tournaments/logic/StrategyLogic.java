package cz.schlosserovi.tomas.drooms.tournaments.logic;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

@RequestScoped
public class StrategyLogic {
    private UserDAO users;
    private StrategyDAO strategies;

    public StrategyLogic() {
    }

    @Inject
    public StrategyLogic(UserDAO users, StrategyDAO strategies) {
        this.users = users;
        this.strategies = strategies;
    }

    public Collection<Strategy> getAllStrategies() {
        return getConverter().convert(strategies.getStrategies());
    }

    public Collection<Strategy> getUserStrategies(String userName) {
        UserEntity user = users.getUser(userName);

        return getConverter().convert(strategies.getStrategies(user));
    }

    public void insertStrategy(String userName, Strategy strategy) {
        StrategyEntity entity = new StrategyEntity();
        entity.setGav(strategy.getGav());
        entity.setAuthor(users.getUserWithStrategies(userName));

        strategies.insertStrategy(entity);

        if (strategy.isActive()) {
            strategies.activateStrategy(entity);
        }
    }

    public void activateStratey(String userName, Strategy strategy) {
        StrategyEntity entity = strategies.getStrategy(strategy.getGav());
        if (entity.getAuthor().getName().equals(userName)) {
            strategies.activateStrategy(entity);
        } else {
            throw new IllegalArgumentException(String.format("Strategy %s does not belong to player %n.", strategy.getGav(),
                    userName));
        }
    }

    private Converter<StrategyEntity, Strategy> getConverter() {
        return getConverter(0);
    }

    private Converter<StrategyEntity, Strategy> getConverter(int depth) {
        return Converter.forClass(StrategyEntity.class).setRecurseDepth(0);
    }
}
