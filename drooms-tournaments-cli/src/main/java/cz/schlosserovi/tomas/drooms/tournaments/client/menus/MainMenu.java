package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class MainMenu extends Menu {

    public MainMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    public Menu show() {
        printHeader(client, console, "");
        if (client.isLoggedIn()) {
            console.format("1. list strategies%n");
//            console.format("1. list playgrounds%n");
            console.format("%n");
            console.format("8. logout%n");
            console.format("9. exit%n");
        } else {
            console.format("1. register new user%n");
            console.format("2. login%n");
            console.format("%n");
            console.format("9. exit%n");
        }
        char res = console.readLine().charAt(0);
        if (res == '9') {
            return exit();
        }
        if (client.isLoggedIn() && res == '1') {
            return new StrategiesMenu(console, client);
        }
        if (client.isLoggedIn() && res == '2') {
            // print playgrounds
        }
        if (client.isLoggedIn() && res == '8') {
            return logout();
        }
        if (!client.isLoggedIn() && res == '1') {
            return new RegisterMenu(console, client);
        }
        if (!client.isLoggedIn() && res == '2') {
            return new LoginMenu(console, client);
        }

        return new MainMenu(console, client);
    }

}
