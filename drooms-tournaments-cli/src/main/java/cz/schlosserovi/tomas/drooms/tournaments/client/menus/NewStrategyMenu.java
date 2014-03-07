package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

class NewStrategyMenu extends Menu {

    protected NewStrategyMenu(Console console, UserServiceClient client) {
        super(console, client);
    }

    @Override
    public Menu show() {
        printHeader(client, console, "- strategies");
        //TODO some information about new strategies
        String groupId = console.readLine("Strategy groupId: ");
        String artifactId = console.readLine("Strategy artifactId: ");
        String version = console.readLine("Strategy version: ");

        client.newStrategy(groupId, artifactId, version);

        return new StrategiesMenu(console, client);
    }

}
