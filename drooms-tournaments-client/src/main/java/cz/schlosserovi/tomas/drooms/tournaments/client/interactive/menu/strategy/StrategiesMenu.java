package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.strategy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.services.StrategyService;

public class StrategiesMenu extends Menu {
    private List<Strategy> strategies;

    public StrategiesMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "strategies";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.STRATEGY_NEW, Choice.STRATEGY_ACTIVATE);
    }

    @Override
    protected void printInstructions() {
        strategies = loadStrategies();

        console.printLine("List of %s's strategies:", client.getLoogedInUser());
        console.printLine(singleLine());
        console.printLine("|   |   |                   groupId | artifactId               |    version    |");
        console.printLine(singleLine());
        for (int i = 1; i <= strategies.size(); i++) {
            Strategy s = strategies.get(i - 1);
            console.print("|%3s", i);
            console.print("| %s ", s.isActive() ? "*" : " ");
            console.print("|%26s ", trimToSize(s.getGav().getGroupId(), 26));
            console.print("| %-25s", trimToSize(s.getGav().getArtifactId(), 25));
            console.printLine("|%15s|", trimToSize(s.getGav().getVersion(), 14));
        }
        console.printLine(singleLine());
    }

    @Override
    public Menu execute(Choice choice) {
        switch (choice) {
        case STRATEGY_NEW:
            return new NewStrategyMenu(console, client);
        case STRATEGY_ACTIVATE:
            int index = parseChoice("Strategy index (see above table): ") - 1;
            if (index >= 0 && index < strategies.size()) {
                client.getService(StrategyService.class).setActiveStrategy(strategies.get(index));
            }
        default:
            return super.execute(choice);
        }
    }

    private List<Strategy> loadStrategies() {
        List<Strategy> strategies = new LinkedList<>();
        strategies.addAll(client.getService(StrategyService.class).getUserStrategies());
        Collections.sort(strategies);

        return strategies;
    }
}
