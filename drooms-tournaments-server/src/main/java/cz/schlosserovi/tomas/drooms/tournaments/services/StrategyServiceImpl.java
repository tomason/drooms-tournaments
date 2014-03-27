package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.spi.BadRequestException;

import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

public class StrategyServiceImpl implements StrategyService {
    private UserDAO users;
    private StrategyDAO strategies;
    @Context
    private SecurityContext security;

    public StrategyServiceImpl() {
    }

    @Inject
    public StrategyServiceImpl(UserDAO users, StrategyDAO strategies) {
        this(users, strategies, null);
    }

    public StrategyServiceImpl(UserDAO users, StrategyDAO strategies, SecurityContext security) {
        this.users = users;
        this.strategies = strategies;
        this.security = security;
    }

    @Override
    public Collection<Strategy> getStrategies() {
        return Converter.forClass(StrategyEntity.class).convert(strategies.getStrategies());
    }

    @Override
    public Collection<Strategy> getUserStrategies() {
        String name = security.getUserPrincipal().getName();
        UserEntity user = users.getUser(name);

        return Converter.forClass(StrategyEntity.class).convert(strategies.getStrategies(user));
    }

    @Override
    public void newStrategy(Strategy strategy) {
        String userName = security.getUserPrincipal().getName();

        try {
            strategies.insertStrategy(userName, strategy.getGav());
        } catch (EntityExistsException ex) {
            throw new BadRequestException("Strategy with this GAV is already registered.");
        }
    }

    @Override
    public void setActiveStrategy(Strategy strategy) {
        strategies.setDefaultStrategy(strategy.getGav());
    }

}
