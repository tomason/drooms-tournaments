package cz.schlosserovi.tomas.drooms.tournaments.services;

import static cz.schlosserovi.tomas.drooms.tournaments.util.ConversionUtil.entityToDomain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.StrategyDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.StrategyEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.ConversionUtil;

public class UserServiceImpl implements UserService {
    @Inject
    private UserDAO users;
    @Inject
    private StrategyDAO strategies;
    @Inject
    private PlaygroundDAO playgrounds;
    @Inject
    private TournamentDAO tournaments;
    @Inject
    private TournamentResultDAO tournamentResults;
    @Context
    private SecurityContext security;

    @Override
    public Response getStrategies() {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            List<Strategy> result = new LinkedList<>();
            for (StrategyEntity entity : strategies.getStrategies(users.getUser(userName))) {
                result.add(entityToDomain(entity));
            }
            builder = Response.ok(result);
        } else {
            builder = Response.status(Status.BAD_REQUEST);
        }
        return builder.build();
    }

    @Override
    public Response newStrategy(Strategy strategy) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            strategies.insertStrategy(userName, strategy.getGav());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response activateStrategy(Strategy strategy) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            strategies.setDefaultStrategy(strategy.getGav());
            builder = Response.status(Status.ACCEPTED);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response getPlaygrounds() {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            List<Playground> result = new LinkedList<>();
            for (PlaygroundEntity entity : playgrounds.getPlaygrounds(users.getUser(userName))) {
                result.add(entityToDomain(entity));
            }
            builder = Response.ok(result);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response newPlayground(Playground playground) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            playgrounds.insertPlayground(userName, playground.getName(), playground.getSource());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response configurePlayground(Playground playground) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            playgrounds.setPlaygroundConfiguration(playground.getName(), playground.getConfiguration());
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response getTournaments() {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            List<Tournament> result = new LinkedList<>();

            List<TournamentEntity> unfinished = tournaments.getUnfinishedTournaments();
            List<TournamentEntity> enrolled = tournaments.getTournaments(users.getUser(userName));
            unfinished.removeAll(enrolled);

            for (TournamentEntity entity : unfinished) {
                result.add(ConversionUtil.entityToDomain(entity, false));
            }
            for (TournamentEntity entity : enrolled) {
                result.add(ConversionUtil.entityToDomain(entity, true));
            }

            builder = Response.ok(result);
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response newTournament(Tournament tournament) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            Collection<String> playgrounds = new LinkedList<>();
            for (Playground playground : tournament.getPlaygrounds()) {
                playgrounds.add(playground.getName());
            }
            tournaments.insertTournament(tournament.getName(), tournament.getStart(), tournament.getEnd(),
                    tournament.getPeriod(), playgrounds);
            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    @Override
    public Response joinTournament(Tournament tournament) {
        ResponseBuilder builder;
        String userName = getUserName();

        if (userName != null) {
            tournamentResults.insertResult(userName, tournament.getName());

            builder = Response.ok();
        } else {
            builder = Response.status(Status.UNAUTHORIZED);
        }
        return builder.build();
    }

    private String getUserName() {
        String userName = null;

        if (security.getUserPrincipal() != null) {
            userName = security.getUserPrincipal().getName();
        }

        return userName;
    }
}
