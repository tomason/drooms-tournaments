package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class LoginMenu extends FormMenu {

    public LoginMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "login";
    }

    @Override
    protected Menu execute(int choice) {
        String username = console.readLine("User name: ");
        String password = new String(console.readPassword("Password: "));

        if (!client.login(username, password)) {
            console.format("Unable to login %s, check your username and password", username);
        }

        return new MainMenu(console, client);
    }

}
