package cz.schlosserovi.tomas.drooms.tournaments.client;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import cz.schlosserovi.tomas.drooms.tournaments.client.batch.BatchClient;
import cz.schlosserovi.tomas.drooms.tournaments.client.common.CommonArguments;
import cz.schlosserovi.tomas.drooms.tournaments.client.executor.ExecutorClient;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.InteractiveClient;

/**
 * Entry point into Drooms Tournaments client
 */
public class Main {

    public static void main(String[] args) {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        ResteasyProviderFactory.getInstance().registerProvider(ResteasyJacksonProvider.class);

        CommonArguments arguments = new CommonArguments(args);
        if (arguments.isHelp()) {
            arguments.printHelp();
        } else if (arguments.isBatchMode()) {
            new BatchClient(args).execute();
        } else if (arguments.isExecutionMode()) {
            new ExecutorClient(args).execute();
        } else {
            new InteractiveClient(args).execute();
        }
    }

}
