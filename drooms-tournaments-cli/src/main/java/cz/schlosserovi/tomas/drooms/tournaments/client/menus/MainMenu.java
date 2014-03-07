package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class MainMenu extends Menu {

    public MainMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "";
    }
    
    @Override
    protected boolean allowMainMenu() {
        return false;
    }

    @Override
    protected void printMenu() {
        if (client.isLoggedIn()) {
            console.format("1. list strategies%n");
            console.format("2. list playgrounds%n");
        } else {
            console.format("1. register new user%n");
            console.format("2. login%n");
        }
    }

    @Override
    protected Menu execute(int choice) {
        if (client.isLoggedIn() && choice == 1) {
            return new StrategiesMenu(console, client);
        }
        if (client.isLoggedIn() && choice == 2) {
            return new PlaygroundsMenu(console, client);
        }
        if (!client.isLoggedIn() && choice == 1) {
            return new RegisterMenu(console, client);
        }
        if (!client.isLoggedIn() && choice == 2) {
            return new LoginMenu(console, client);
        }

        return new MainMenu(console, client);
    }

}
