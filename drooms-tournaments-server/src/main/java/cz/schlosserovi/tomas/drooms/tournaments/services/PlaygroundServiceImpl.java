package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.logic.PlaygroundLogic;

public class PlaygroundServiceImpl implements PlaygroundService {
    private PlaygroundLogic logic;
    @Context
    private SecurityContext security;

    public PlaygroundServiceImpl() {
    }

    @Inject
    public PlaygroundServiceImpl(PlaygroundLogic logic) {
        this(logic, null);
    }

    public PlaygroundServiceImpl(PlaygroundLogic logic, SecurityContext security) {
        this.logic = logic;
        this.security = security;
    }

    @Override
    public Collection<Playground> getPlaygrounds() {
        return logic.getAllPlaygrounds();
    }

    @Override
    public Collection<Playground> getUserPlaygrounds() {
        return logic.getUserPlaygrounds(security.getUserPrincipal().getName());
    }

    @Override
    public void newPlayground(Playground playground) {
        logic.insertPlayground(security.getUserPrincipal().getName(), playground);
    }

}
