package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

public class ConfigurePlaygroundMenu extends Menu {
    private String playgroundName;
    private Properties configuration;
    private List<String> keys = new LinkedList<>();

    protected ConfigurePlaygroundMenu(Console console, UserServiceClient client, String playgroundName, Properties configuration) {
        super(console, client);
        this.playgroundName = playgroundName;
        this.configuration = configuration;
        keys.addAll(configuration.stringPropertyNames());
    }

    @Override
    protected String getHeadline() {
        return "configure " + playgroundName;
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
            console.format("| %-36s|%n", configuration.getProperty(key));
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
        configuration.setProperty(key, value);
        keys.add(key);
    }

    private void removeConfiguration() {
        int choice = parseChoice("Configuration index (see above table): ") - 1;
        if (choice >= 0 && choice < keys.size()) {
            configuration.remove(keys.get(choice));
            keys.remove(choice);
        }
    }

    private void editConfiguration() {
        int choice = parseChoice("Configuration index (see above table): ") - 1;
        if (choice >= 0 && choice < keys.size()) {
            String value = console.readLine("New value (%s): ", keys.get(choice));
            configuration.setProperty(keys.get(choice), value);
        }
    }

    private Menu discardConfiguration() {
        return new PlaygroundsMenu(console, client);
    }

    private Menu saveConfiguration() {
        client.configurePlayground(playgroundName, configuration);
        return new PlaygroundsMenu(console, client);
    }
}
