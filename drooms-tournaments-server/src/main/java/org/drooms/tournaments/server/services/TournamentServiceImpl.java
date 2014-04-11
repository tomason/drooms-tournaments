package org.drooms.tournaments.server.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.server.logic.TournamentLogic;
import org.drooms.tournaments.services.TournamentService;

public class TournamentServiceImpl implements TournamentService {
    private TournamentLogic logic;
    @Context
    private SecurityContext security;

    public TournamentServiceImpl() {
    }

    @Inject
    public TournamentServiceImpl(TournamentLogic logic) {
        this(logic, null);
    }

    public TournamentServiceImpl(TournamentLogic logic, SecurityContext security) {
        this.logic = logic;
        this.security = security;
    }

    @Override
    public Collection<Tournament> getTournaments() {
        return logic.getAllTournaments();
    }

    @Override
    public Tournament getTournament(String tournamentName) {
        return logic.getTournamentDetail(tournamentName);
    }

    @Override
    public Collection<Tournament> getUserTournaments() {
        String userName = security.getUserPrincipal().getName();

        return logic.getUserTournaments(userName);
    }

    @Override
    public void newTournament(Tournament tournament) {
        logic.newTournament(tournament);
    }

    @Override
    public void joinTournament(Tournament tournament) {
        String userName = security.getUserPrincipal().getName();

        logic.joinTournament(userName, tournament);
    }

}
