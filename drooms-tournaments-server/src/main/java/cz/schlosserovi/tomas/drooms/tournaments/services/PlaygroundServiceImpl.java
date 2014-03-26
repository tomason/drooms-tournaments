package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.schlosserovi.tomas.drooms.tournaments.data.PlaygroundDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.UserDAO;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.model.PlaygroundEntity;
import cz.schlosserovi.tomas.drooms.tournaments.model.UserEntity;
import cz.schlosserovi.tomas.drooms.tournaments.util.Converter;

public class PlaygroundServiceImpl implements PlaygroundService {
    private UserDAO users;
    private PlaygroundDAO playgrounds;
    @Context
    private SecurityContext security;

    public PlaygroundServiceImpl() {
    }

    @Inject
    public PlaygroundServiceImpl(UserDAO users, PlaygroundDAO playgrounds) {
        this(users, playgrounds, null);
    }

    public PlaygroundServiceImpl(UserDAO users, PlaygroundDAO playgrounds, SecurityContext security) {
        this.users = users;
        this.playgrounds = playgrounds;
        this.security = security;
    }

    @Override
    public Response getPlaygrounds() {
        return Response.ok(Converter.forClass(PlaygroundEntity.class).convert(playgrounds.getPlaygrounds())).build();
    }

    @Override
    public Response getUserPlaygrounds() {
        String userName = security.getUserPrincipal().getName();

        UserEntity user = users.getUser(userName);
        List<Playground> result = Converter.forClass(PlaygroundEntity.class).convert(playgrounds.getPlaygrounds(user));

        return Response.ok(result).build();
    }

    @Override
    public Response newPlayground(Playground playground) {
        String userName = security.getUserPrincipal().getName();

        playgrounds.insertPlayground(userName, playground.getName(), playground.getSource());
        playgrounds.setPlaygroundConfiguration(playground.getName(), playground.getConfiguration());

        return Response.ok().build();
    }

}
