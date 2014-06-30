package org.drooms.tournaments.client;

import org.drooms.tournaments.client.batch.BatchClient;
import org.drooms.tournaments.client.common.CommonArguments;
import org.drooms.tournaments.client.executor.ExecutorClient;
import org.drooms.tournaments.client.interactive.InteractiveClient;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * Entry point into Drooms Tournaments client
 */
public class Main {

    public static void main(String[] args) {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        ResteasyProviderFactory.getInstance().registerProvider(ResteasyJacksonProvider.class);

        // avoid checking SSL certificate validity
        System.setProperty("jsse.enableSNIExtension", "false");

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
