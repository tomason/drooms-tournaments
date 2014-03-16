package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;

abstract class BackReferrenceMenu extends Menu {
    private final Menu previous;

    protected BackReferrenceMenu(Console console, TournamentsServerClient client, Menu previous) {
        super(console, client);
        this.previous = previous;
    }

    @Override
    protected void printBasicChoices() {
        console.format("6. back%n");
        super.printBasicChoices();
    }

    @Override
    protected Menu executeBasicChoices(int choice) {
        if (choice == 6) {
            back();
        }

        return super.executeBasicChoices(choice);
    }

    protected Menu back() {
        return previous;
    }
}
