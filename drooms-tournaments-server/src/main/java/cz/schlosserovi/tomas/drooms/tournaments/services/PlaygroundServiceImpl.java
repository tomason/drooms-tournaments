package cz.schlosserovi.tomas.drooms.tournaments.services;

import static cz.schlosserovi.tomas.drooms.tournaments.util.ConversionUtil.entityToDomain;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;

public class PlaygroundServiceImpl implements PlaygroundService {
    @Inject
    private PlaygroundDAO playgrounds;
//    @Inject
//    private TournamentDAO tournaments;

    @Override
    public Response getPlaygrounds() {
        List<Playground> result = new LinkedList<>();
        for (PlaygroundEntity entity : playgrounds.getPlaygrounds()) {
            result.add(entityToDomain(entity));
        }

        return Response.ok(result).build();
    }

//    @Override
//    public Response getPlaygrounds(Tournament tournament) {
//        List<Playground> result = new LinkedList<>();
//        for (PlaygroundEntity entity : playgrounds.getPlaygrounds(tournaments.getTournament(tournament.getName()))) {
//            result.add(entityToDomain(entity));
//        }
//
//        return Response.ok(result).build();
//    }

}
