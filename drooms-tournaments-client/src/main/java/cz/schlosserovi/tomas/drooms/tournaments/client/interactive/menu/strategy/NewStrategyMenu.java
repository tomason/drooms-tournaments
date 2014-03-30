package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.strategy;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.FormMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.services.StrategyService;

class NewStrategyMenu extends FormMenu {

    protected NewStrategyMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "new strategy";
    }

    @Override
    protected void printInstructions() {
        // TODO some information about new strategies
    }

    @Override
    protected Menu execute(Choice choice) {
        String groupId = console.readLine("Strategy groupId: ");
        String artifactId = console.readLine("Strategy artifactId: ");
        String version = console.readLine("Strategy version: ");

        client.getService(StrategyService.class).newStrategy(new Strategy(groupId, artifactId, version));

        return new StrategiesMenu(console, client);
    }

}
