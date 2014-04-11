package org.drooms.tournaments.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.drooms.tournaments.domain.Strategy;

/**
 * This service provides access to Strategies on the server.
 */
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StrategyService {

    /**
     * Retrieves all strategies on the server.
     * 
     * @return Collection of strategies on the server.
     */
    @GET
    @Path("/strategies")
    public Collection<Strategy> getStrategies();

    /**
     * Retrieves all the strategies beloging to authenticated user.
     * 
     * @return Collection of user's strategies.
     */
    @GET
    @Path("/auth/strategies")
    public Collection<Strategy> getUserStrategies();

    /**
     * Adds a new strategy to the authenticated user.
     * 
     * @param strategy
     *            New strategy.
     */
    @POST
    @Path("/auth/strategies")
    public void newStrategy(Strategy strategy);

    /**
     * Selects the strategy as user's active one.
     * 
     * @param strategy
     *            Strategy to be set as active.
     */
    @PUT
    @Path("/auth/strategies")
    public void setActiveStrategy(Strategy strategy);

}
