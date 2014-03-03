package cz.schlosserovi.tomas.drooms.tournaments.services;

import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.ClientResponseType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserService {

    @PUT
    @Path("/{username}")
    @ClientResponseType(entityType = Void.class)
    public Response register(@PathParam("username") String userName, byte[] password);

    @POST
    @Path("/{username}")
    @ClientResponseType(entityType = String.class)
    public Response login(@PathParam("username") String userName, byte[] password);

    @POST
    @Path("/{username}")
    @ClientResponseType(entityType = String.class)
    public Response logout(@PathParam("username") String userName);

    @GET
    @Path("/strategies")
    @ClientResponseType(entityType = List.class)
    public Response getStrategies(@HeaderParam("Auth-Token") String token);

    @PUT
    @Path("/strategies/{gav}")
    @ClientResponseType(entityType = Void.class)
    public Response newStrategy(@HeaderParam("Auth-Token") String token, @PathParam("gav") GAV gav);

    @PUT
    @Path("/strategies/{gav}/activate")
    @ClientResponseType(entityType = Void.class)
    public Response activateStrategy(@HeaderParam("Auth-Token") String token, @PathParam("gav") GAV gav);

    @GET
    @Path("/playgrounds")
    @ClientResponseType(entityType = List.class)
    public Response getPlaygrounds(@HeaderParam("Auth-Token") String token);

    @PUT
    @Path("/playgrounds/{playground}")
    @ClientResponseType(entityType = Void.class)
    public Response insertOrUpdatePlayground(@HeaderParam("Auth-Token") String token,
            @PathParam("playground") String playgroundName, String source);

    @PUT
    @Path("/playgrounds/{playground}/configure")
    @ClientResponseType(entityType = Void.class)
    public Response configurePlayground(@HeaderParam("Auth-Token") String token,
            @PathParam("playground") String playgroundName, Properties config);
}
