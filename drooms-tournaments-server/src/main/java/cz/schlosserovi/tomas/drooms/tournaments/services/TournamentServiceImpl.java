package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.logic.TournamentLogic;

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
    public Response getTournaments() {
        return Response.ok(logic.getAllTournaments()).build();
    }

    @Override
    public Response getUserTournaments() {
        String userName = security.getUserPrincipal().getName();

        return Response.ok(logic.getUserTournaments(userName)).build();
    }

    @Override
    public Response newTournament(Tournament tournament) {
        logic.newTournament(tournament);

        return Response.ok().build();
    }

    @Override
    public Response joinTournament(Tournament tournament) {
        String userName = security.getUserPrincipal().getName();

        logic.joinTournament(userName, tournament);

        return Response.ok().build();
    }

}
