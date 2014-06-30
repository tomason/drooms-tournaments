package org.drooms.tournaments.client.interactive;

import org.apache.commons.codec.binary.Base64;
import org.drooms.tournaments.client.interactive.menu.ExitMenu;
import org.drooms.tournaments.client.interactive.menu.MainMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;

/**
 * Client in interactive mode is used for user's interaction (registration, new
 * strategies/playgrounds/tournaments, tournament enrollment). This part was
 * taken from the old cli module and redisigned a bit.
 */
public class InteractiveClient {
    private static final String DEFAULT_DROOMS_SERVER = "tournaments-drooms.rhcloud.com";
    private final InteractiveCommandLine arguments;

    public InteractiveClient(String[] args) {
        arguments = new InteractiveCommandLine(args);
    }

    public void execute() {
        OutputDevice console = OutputDevice.Factory.getDevice();
        String serverName = DEFAULT_DROOMS_SERVER;
        if (arguments.getServer() == null) {
            console.print("What Drooms tournaments server would you like to connect to?%n");
            String input = console.readLine("URL (%s): ", DEFAULT_DROOMS_SERVER).trim();
            if (input.length() > 0) {
                serverName = input;
            }
        } else {
            serverName = arguments.getServer();
        }
        TournamentsServerClient client = new TournamentsServerClient(serverName);

        if (arguments.getCredentials() != null) {
            client.login(new String(Base64.decodeBase64(arguments.getCredentials())));
        }

        Menu menu = new MainMenu(console, client);
        while (!(menu instanceof ExitMenu)) {
            try {
                menu = menu.show();
            } catch (Exception ex) {
                console.print("Error in application:%n %s", ex);
                ex.printStackTrace();
                console.print("%n%n");
                menu = new MainMenu(console, client);
            }
        }
    }
}
