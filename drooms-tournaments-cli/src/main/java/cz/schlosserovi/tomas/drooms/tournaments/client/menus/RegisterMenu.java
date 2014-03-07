package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.nio.charset.StandardCharsets;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class RegisterMenu extends Menu {

    public RegisterMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    public Menu show() {
        printHeader(client, console, "- registration");
        // TODO some information for new users
        String username = console.readLine("User name: ");
        String password = new String(console.readPassword("Password: "));

        client.register(username, password.getBytes(StandardCharsets.UTF_8));

        return new MainMenu(console, client);
    }

}
