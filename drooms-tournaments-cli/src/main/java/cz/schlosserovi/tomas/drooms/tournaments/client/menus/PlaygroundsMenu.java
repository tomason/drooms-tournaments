package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

public class PlaygroundsMenu extends Menu {
    private List<Playground> playgrounds;

    protected PlaygroundsMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "playgrounds";
    }

    @Override
    protected boolean allowMainMenu() {
        return true;
    }

    @Override
    protected void printMenu() {
        console.format("List of %s's playgrounds:%n", client.getLoogedInUser());
        console.format("%s%n", SINGLE_LINE);
        console.format("|   |                           name                                 | players |%n");
        console.format("%s%n", SINGLE_LINE);
        playgrounds = new LinkedList<>(client.getPlaygrounds());
        for (int i = 1; i <= playgrounds.size(); i++) {
            Playground p = playgrounds.get(i - 1);
            console.format("|%3s", i);
            console.format("|%63s ", trimToSize(p.getName(), 64));
            console.format("|%8s |%n", p.getMaxPlayers());
        }
        console.format("%s%n", SINGLE_LINE);
        console.format("1. new Playground%n");
        console.format("2. configure Playground%n");
    }

    @Override
    protected Menu execute(int choice) {
        switch (choice) {
        case 1:
            return new NewPlaygroundMenu(console, client);
        case 2:
            int index = parseChoice("Playground index (see above table): ") - 1;
            if (index >= 0 && index < playgrounds.size()) {
                Playground p = playgrounds.get(index);
                return new ConfigurePlaygroundMenu(console, client, p);
            }
            break;
        }

        return new PlaygroundsMenu(console, client);
    }
}
