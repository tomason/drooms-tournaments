package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.ExecutionResults;
import cz.schlosserovi.tomas.drooms.tournaments.domain.ExecutionSetup;

@Path("/services/execution")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ExecutorService {

    @GET
    @Path("/register")
    @ClientResponseType(entityType = String.class)
    public Response register();

    @POST
    @Path("/ping")
    @ClientResponseType(entityType = Void.class)
    public Response ping(@HeaderParam("Auth-Token") String id);

    @POST
    @Path("/getWork")
    @ClientResponseType(entityType = ExecutionSetup.class)
    public Response getNewGame(@HeaderParam("Auth-Token") String id);

    @PUT
    @Path("/return")
    @ClientResponseType(entityType = Void.class)
    public Response returnGameResults(@HeaderParam("Auth-Token") String id, ExecutionResults results);

}
