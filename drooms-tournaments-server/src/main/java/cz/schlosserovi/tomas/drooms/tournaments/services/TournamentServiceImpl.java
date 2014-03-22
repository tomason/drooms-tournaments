package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

public class TournamentServiceImpl implements TournamentService {
    @Inject
    private UserDAO users;
    @Inject
    private TournamentDAO tournaments;
    @Inject
    private TournamentResultDAO tournamentResults;
    @Context
    private SecurityContext security;

    @Override
    public Response getTournaments() {
        return Response.ok(Converter.forClass(TournamentEntity.class).convert(tournaments.getTournaments())).build();
    }

    @Override
    public Response getUserTournaments() {
        String userName = security.getUserPrincipal().getName();

        List<Tournament> result = new LinkedList<>();

        List<TournamentEntity> unfinished = tournaments.getUnfinishedTournaments();
        List<TournamentEntity> enrolled = tournaments.getTournaments(users.getUser(userName));
        unfinished.removeAll(enrolled);

        Converter<TournamentEntity, Tournament> converter = Converter.forClass(TournamentEntity.class);
        result.addAll(converter.convert(unfinished));
        result.addAll(converter.convert(enrolled));

        return Response.ok(result).build();
    }

    @Override
    public Response newTournament(Tournament tournament) {
        Collection<String> playgrounds = new LinkedList<>();
        for (Playground playground : tournament.getPlaygrounds()) {
            playgrounds.add(playground.getName());
        }

        tournaments.insertTournament(tournament.getName(), tournament.getStart(), tournament.getEnd(), tournament.getPeriod(),
                playgrounds);

        return Response.ok().build();
    }

    @Override
    public Response joinTournament(Tournament tournament) {
        String userName = security.getUserPrincipal().getName();

        tournamentResults.insertResult(userName, tournament.getName());

        return Response.ok().build();
    }

}
