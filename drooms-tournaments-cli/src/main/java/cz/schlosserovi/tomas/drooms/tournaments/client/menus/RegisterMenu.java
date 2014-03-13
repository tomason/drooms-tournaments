package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;

class RegisterMenu extends FormMenu {

    public RegisterMenu(Console console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "registration";
    }

    @Override
    protected Menu execute(int choice) {
        String username = console.readLine("User name: ");
        String password = new String(console.readPassword("Password: "));

        client.register(username, password);

        return new MainMenu(console, client);
    }

}
