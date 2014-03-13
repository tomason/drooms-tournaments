package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

public class ConfigurePlaygroundMenu extends Menu {
    private Playground playground;
    private List<String> keys = new LinkedList<>();

    protected ConfigurePlaygroundMenu(Console console, TournamentsServerClient client, Playground playground) {
        super(console, client);
        this.playground = playground;
        if (playground.getConfiguration() == null) {
            playground.setConfiguration(new Properties());
        }
        keys.addAll(playground.getConfiguration().stringPropertyNames());
    }

    @Override
    protected String getHeadline() {
        return "configure " + playground.getName();
    }

    @Override
    protected boolean allowMainMenu() {
        return false;
    }

    @Override
    protected void printMenu() {
        console.format("Current configuration:%n");
        console.format("%s%n", SINGLE_LINE);
        console.format("|   |                                key | value                               |%n");
        console.format("%s%n", SINGLE_LINE);
        int i = 1;
        for (String key : keys) {
            console.format("|%3s", i++);
            console.format("|%35s ", key);
            console.format("| %-36s|%n", playground.getConfiguration().getProperty(key));
        }
        console.format("%s%n", SINGLE_LINE);
        console.format("1. Add configuration%n");
        console.format("2. Remove configuration%n");
        console.format("3. Edit configuration%n");
        console.format("4. Save configuration%n");
        console.format("5. Throw away changes and return to Playgrounds menu%n");
    }

    @Override
    protected Menu execute(int choice) {
        switch (choice) {
        case 1:
            newProperty();
            break;
        case 2:
            removeConfiguration();
            break;
        case 3:
            editConfiguration();
            break;
        case 4:
            return saveConfiguration();
        case 5:
            return discardConfiguration();
        }

        return this;
    }

    private void newProperty() {
        String key = console.readLine("Configuration key: ");
        String value = console.readLine("Configuration value: ");
        playground.getConfiguration().setProperty(key, value);
        keys.add(key);
    }

    private void removeConfiguration() {
        int choice = parseChoice("Configuration index (see above table): ") - 1;
        if (choice >= 0 && choice < keys.size()) {
            playground.getConfiguration().remove(keys.get(choice));
            keys.remove(choice);
        }
    }

    private void editConfiguration() {
        int choice = parseChoice("Configuration index (see above table): ") - 1;
        if (choice >= 0 && choice < keys.size()) {
            String value = console.readLine("New value (%s): ", keys.get(choice));
            playground.getConfiguration().setProperty(keys.get(choice), value);
        }
    }

    private Menu discardConfiguration() {
        return new PlaygroundsMenu(console, client);
    }

    private Menu saveConfiguration() {
        client.configurePlayground(playground);
        return new PlaygroundsMenu(console, client);
    }
}
