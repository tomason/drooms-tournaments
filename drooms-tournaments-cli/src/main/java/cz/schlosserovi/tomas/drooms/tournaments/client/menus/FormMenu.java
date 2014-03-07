package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

abstract class FormMenu extends Menu {

    protected FormMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    protected void printMenu() {
    }

    @Override
    protected boolean allowMainMenu() {
        return false;
    }

    @Override
    protected boolean showMenu() {
        return false;
    }
}
