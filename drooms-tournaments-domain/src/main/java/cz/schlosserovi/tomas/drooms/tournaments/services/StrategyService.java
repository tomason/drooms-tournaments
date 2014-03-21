package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;

@Path("/services/strategies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StrategyService {

    @PermitAll
    @GET
    public Response getStrategies(@QueryParam("only-active") @DefaultValue("false") boolean onlyActive);

    @GET
    //@Path("/auth")
    public Response getUserStrategies();

    @POST
    //@Path("/auth")
    public Response newStrategy(Strategy strategy);

    @PUT
    //@Path("/auth")
    public Response setActiveStrategy(Strategy strategy);

}
