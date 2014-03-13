package cz.schlosserovi.tomas.drooms.tournaments.client;

import java.io.Console;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import cz.schlosserovi.tomas.drooms.tournaments.client.menus.Menu;

public class DroomsCLI {
    private static final String DEFAULT_DROOMS_SERVER = "tournaments-drooms.rhcloud.com";

    public static void main(String[] args) {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

        Console console = System.console();
        String serverName = DEFAULT_DROOMS_SERVER;
        if (args.length < 1) {
            console.format("What Drooms tournaments server would you like to connect to?%n");
            String input = console.readLine("URL (%s): ", DEFAULT_DROOMS_SERVER);
            if (input.trim().length() > 0) {
                serverName = input.trim();
            }
        } else {
            serverName = args[0];
        }
        TournamentsServerClient client = new TournamentsServerClient(serverName);

        Menu menu = Menu.getMainMenu(console, client);
        while (menu != null) {
            try {
                menu = menu.show();
            } catch (IllegalStateException | ResponseException ex) {
                // my exception with message from the webapp
                console.format("%n%s%n%n", ex.getMessage());
            } catch (Exception ex) {
                // any other exception
                console.format("Error in application:%n %s%n%n", ex);
                menu = Menu.getMainMenu(console, client);
            }
        }
    }

}
