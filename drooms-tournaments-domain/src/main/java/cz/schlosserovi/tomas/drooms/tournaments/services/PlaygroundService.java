package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

@Path("/services/playgrounds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PlaygroundService {

    @GET
    @ClientResponseType(entityType = List.class)
    public Response getPlaygrounds();

//    @GET
//    @ClientResponseType(entityType = List.class)y
//    public Response getPlaygrounds(@HeaderParam("user") User user);

//    @GET
//    @Path("/byTournament")
//    @ClientResponseType(entityType = List.class)
//    public Response getPlaygrounds(@HeaderParam("tournament") Tournament tournament);
}
