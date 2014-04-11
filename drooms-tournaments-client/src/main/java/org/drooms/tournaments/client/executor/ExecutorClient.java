package org.drooms.tournaments.client.executor;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client in execution mode is used for running the games on the client
 * machines. Using {@link GameService} interface it gets game queue, executes
 * one of the games and delivers the results back to server. Requires server and
 * credentials to be set. The client ends with the first exception. This
 * behavior may be changed in the future causing the client to only be stopped
 * by killing the application.
 */
public class ExecutorClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorClient.class);
    private ExecutorCommandLine arguments;

    public ExecutorClient(String[] args) {
        arguments = new ExecutorCommandLine(args);
    }

    public void execute() {
        if (arguments.isHelp()) {
            arguments.printHelp();
        } else {
            TournamentsServerClient client = new TournamentsServerClient(arguments.getServer());
            client.login(arguments.getCredentials());

            GameService service = client.getService(GameService.class);
            LOGGER.info("Starting execution...");

            while (true) {
                List<Game> gameQueue = new LinkedList<>(service.getExecutionQueue());
                if (gameQueue.size() > 0) {
                    LOGGER.info("Game queue is not empty");
                    Game chosenOne = gameQueue.get(new SecureRandom().nextInt(gameQueue.size()));
                    service.reserveExecution(chosenOne);

                    LOGGER.info("Game selected and reserved, beginning execution");
                    GameLauncher launcher = new GameLauncher(chosenOne);
                    service.deliverResults(launcher.play());
                    LOGGER.info("Execution finished");
                } else {
                    LOGGER.info("Game queue is empty, waiting 10 minutes for another try");
                    try {
                        Thread.sleep(600_000L);
                    } catch (InterruptedException ex) {
                        // ignored deliberately
                    }
                }
            }
        }
    }
}
