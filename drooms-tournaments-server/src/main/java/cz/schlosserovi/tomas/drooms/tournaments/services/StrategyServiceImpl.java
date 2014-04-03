package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.spi.BadRequestException;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.logic.StrategyLogic;

public class StrategyServiceImpl implements StrategyService {
    private StrategyLogic logic;
    @Context
    private SecurityContext security;

    public StrategyServiceImpl() {
    }

    @Inject
    public StrategyServiceImpl(StrategyLogic logic) {
        this(logic, null);
    }

    public StrategyServiceImpl(StrategyLogic logic, SecurityContext security) {
        this.logic = logic;
        this.security = security;
    }

    @Override
    public Collection<Strategy> getStrategies() {
        return logic.getAllStrategies();
    }

    @Override
    public Collection<Strategy> getUserStrategies() {
        return logic.getUserStrategies(security.getUserPrincipal().getName());
    }

    @Override
    public void newStrategy(Strategy strategy) {
        try {
            logic.insertStrategy(security.getUserPrincipal().getName(), strategy);
        } catch (Exception ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void setActiveStrategy(Strategy strategy) {
        try {
            logic.activateStratey(security.getUserPrincipal().getName(), strategy);
        } catch (Exception ex) {
            throw new BadRequestException(ex);
        }
    }

}
