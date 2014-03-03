package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import cz.schlosserovi.tomas.drooms.tournaments.beans.GameRegistryBean;
import cz.schlosserovi.tomas.drooms.tournaments.beans.UserRegistryBean;
import cz.schlosserovi.tomas.drooms.tournaments.domain.ExecutionResults;

@Path("/execution")
public class ExecutorServiceImpl implements ExecutorService {
    @Inject
    private UserRegistryBean userRegistry;
    @Inject
    private GameRegistryBean gameRegistry;

    @Override
    public Response register() {
        ResponseBuilder builder;
        try {
            builder = Response.ok(userRegistry.register().toString(), MediaType.TEXT_PLAIN);
        } catch (Exception ex) {
            builder = Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN);
            builder.entity(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }

        return builder.build();
    }

    @Override
    public Response ping(@PathParam("id") String id) {
        ResponseBuilder builder;
        try {
            userRegistry.ping(UUID.fromString(id));

            builder = Response.ok();
        } catch (Exception ex) {
            builder = Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN);
            builder.entity(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }

        return builder.build();
    }

    @Override
    public Response getNewGame(@PathParam("id") String id) {
        ResponseBuilder builder;
        try {
            ping(id);
            builder = Response.ok(gameRegistry.getNewGame(UUID.fromString(id)), MediaType.APPLICATION_JSON);
        } catch (Exception ex) {
            builder = Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN);
            builder.entity(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }

        return builder.build();
    }

    @Override
    public Response returnGameResults(@PathParam("id") String id, ExecutionResults results) {
        ResponseBuilder builder;
        try {
            ping(id);
            gameRegistry.deliverResults(results);
            builder = Response.ok();
        } catch (Exception ex) {
            builder = Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN);
            builder.entity(String.format("%s: %s", ex.getClass().getName(), ex.getMessage()));
        }

        return builder.build();
    }
}
