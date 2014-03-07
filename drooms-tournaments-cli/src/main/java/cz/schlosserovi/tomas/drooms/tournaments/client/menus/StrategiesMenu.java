package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;

class StrategiesMenu extends Menu {

    public StrategiesMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    public Menu show() {
        printHeader(client, console, "- strategies");

        console.format("List of %s's strategies:%n", client.getLoogedInUser());
        console.format("%s%n", SINGLE_LINE);
        console.format("|   |   |                   groupId | artifactId               |    version    |%n");
        console.format("%s%n", SINGLE_LINE);
        List<Strategy> strategies = new LinkedList<>(client.getStrategies());
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
        console.format("%n");
        console.format("8. logout%n");
        console.format("9. exit%n");

        char res = console.readLine().charAt(0);
        switch (res) {
            case '9':
                return exit();
            case '8':
                return logout();
            case '2':
                setActiveStrategy(strategies);
                break;
            case '1':
                return new NewStrategyMenu(console, client);
        }

        return new StrategiesMenu(console, client);
    }

    private void setActiveStrategy(List<Strategy> strategies) {
        String lineNumber = console.readLine("Strategy index (see above table): ");
        try {
            int index = Integer.parseInt(lineNumber) - 1;
            if (index >= 0 && index < strategies.size()) {
                client.setActiveStrategy(strategies.get(index));
            }
        } catch (NumberFormatException ex) {
            // TODO error handling
            // let it be for now
        }
    }
}
