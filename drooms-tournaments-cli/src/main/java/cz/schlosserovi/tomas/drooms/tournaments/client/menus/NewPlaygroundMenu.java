package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.Properties;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

public class NewPlaygroundMenu extends FormMenu {
    private String name;
    private String original;
    private Properties configuration;

    protected NewPlaygroundMenu(Console console, UserServiceClient client) {
        this(console, client, null);
    }

    protected NewPlaygroundMenu(Console console, UserServiceClient client, Playground playground) {
        super(console, client);
        if (playground != null) {
            this.name = playground.getName();
            this.original = playground.getSource();
            this.configuration = playground.getConfiguration();
        } else {
            configuration = new Properties();
        }
    }

    @Override
    protected String getHeadline() {
        return (original == null ? "new playground" : "edit " + name);
    }

    @Override
    protected Menu execute(int choice) {
        // TODO information about new playgrounds
        if (name == null) {
            name = console.readLine("New playground name: ");
        }
        if (original != null) {
            console.format("Original layout:%n%s%n", original);
        }
        console.format("New layout (hit Ctrl+D to send EOF to stop reading:%n");
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = console.readLine()) != null) {
            sb.append(line).append("\n");
        }

        client.newPlayground(name, sb.toString());

        return new ConfigurePlaygroundMenu(console, client, name, configuration);
    }

}
