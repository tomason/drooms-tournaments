package cz.schlosserovi.tomas.drooms.tournaments.client;

import java.io.Console;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import cz.schlosserovi.tomas.drooms.tournaments.client.menus.Menu;

public class DroomsCLI {

    public static void main(String[] args) {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

        if (args.length < 1) {
            System.out.println("Usage: DroomsCLI <drooms-server>");
            System.out.println("       mvn exec:java -Dexec.args=\"<drooms-server>\"");
            System.exit(-1);
        }
        UserServiceClient client = new UserServiceClient(args[0]);
        Console console = System.console();

        Menu menu = Menu.getMainMenu(console, client);
        while (menu != null) {
            try {
                menu = menu.show();
            } catch (Exception ex) {
                // TODO error handling
                console.format("Error in application:%n %s%n%n", ex);
                menu = Menu.getMainMenu(console, client);
            }
        }
    }

}
