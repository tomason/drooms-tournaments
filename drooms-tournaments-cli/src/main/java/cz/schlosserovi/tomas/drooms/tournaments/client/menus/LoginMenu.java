package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.nio.charset.StandardCharsets;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class LoginMenu extends Menu {

    public LoginMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    public Menu show() {
        printHeader(client, console, "- login");
        String username = console.readLine("User name: ");
        String password = new String(console.readPassword("Password: "));

        if (!client.login(username, password.getBytes(StandardCharsets.UTF_8))) {
            console.format("Unable to login %s, check your username and password", username);
        }

        return new MainMenu(console, client);
    }

}
