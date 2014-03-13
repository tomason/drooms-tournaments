package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.List;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;

class StrategiesMenu extends Menu {
    private List<Strategy> strategies;

    public StrategiesMenu(Console console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "strategies";
    }

    @Override
    protected boolean allowMainMenu() {
        return true;
    }

    @Override
    protected void printMenu() {
        console.format("List of %s's strategies:%n", client.getLoogedInUser());
        console.format("%s%n", SINGLE_LINE);
        console.format("|   |   |                   groupId | artifactId               |    version    |%n");
        console.format("%s%n", SINGLE_LINE);
        strategies = client.getStrategies();
        for (int i = 1; i <= strategies.size(); i++) {
            Strategy s = strategies.get(i - 1);
            console.format("|%3s", i);
            console.format("| %s ", s.isActive() ? "*" : " ");
            console.format("|%26s ", trimToSize(s.getGav().getGroupId(), 26));
            console.format("| %-25s", trimToSize(s.getGav().getArtifactId(), 25));
            console.format("|%15s|%n", trimToSize(s.getGav().getVersion(), 14));
        }
        console.format("%s%n", SINGLE_LINE);
        console.format("1. new Strategy%n");
        console.format("2. set active Strategy%n");
    }

    @Override
    public Menu execute(int choice) {
        switch (choice) {
        case 1:
            return new NewStrategyMenu(console, client);
        case 2:
            int index = parseChoice("Strategy index (see above table): ") - 1;
            if (index >= 0 && index < strategies.size()) {
                client.setActiveStrategy(strategies.get(index));
            }
            break;
        }

        return new StrategiesMenu(console, client);
    }

}
