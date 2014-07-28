package org.drooms.tournaments.server.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.drooms.tournaments.domain.Strategy;
import org.drooms.tournaments.server.logic.StrategyLogic;
import org.drooms.tournaments.services.StrategyService;
import org.jboss.resteasy.spi.BadRequestException;

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
    public Collection<Strategy> getStrategies(String username) {
        return logic.getStrategies(username);
    }

    @Override
    public Collection<Strategy> getUserStrategies() {
        return getStrategies(security.getUserPrincipal().getName());
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
