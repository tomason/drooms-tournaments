package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.playground;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

class ConfigurePlaygroundMenu extends BackReferrenceMenu {
    private Playground playground;
    private List<String> keys = new LinkedList<>();

    protected ConfigurePlaygroundMenu(OutputDevice console, TournamentsServerClient client, Menu previous, Playground playground) {
        super(console, client, previous);
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
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.CONFIGURATION_ADD, Choice.CONFIGURATION_EDIT,
                Choice.CONFIGURATION_REMOVE, Choice.CONFIGURATION_COLLECTIBLE, Choice.DONE);
    }

    @Override
    protected void printInstructions() {
        console.printLine("Current configuration:");
        console.printLine(SINGLE_LINE);
        console.printLine("|   |                                key | value                               |");
        console.printLine(SINGLE_LINE);
        int i = 1;
        for (String key : keys) {
            console.print("|%3s", i++);
            console.print("|%35s ", key);
            console.printLine("| %-36s|", playground.getConfiguration().getProperty(key));
        }
        console.print("%s%n", SINGLE_LINE);
    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case CONFIGURATION_ADD:
            newProperty();
            break;
        case CONFIGURATION_EDIT:
            editConfiguration();
            break;
        case CONFIGURATION_REMOVE:
            removeConfiguration();
            break;
        case CONFIGURATION_COLLECTIBLE:
            createCollectible();
            break;
        case DONE:
            return new PlaygroundConfirmationMenu(console, client, this, playground);
        default:
            return super.execute(choice);
        }

        return this;
    }

    private void newProperty() {
        String key = console.readLine("Configuration key: ");
        String value = console.readLine("Configuration value: ");
        setProperty(key, value);
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

            setProperty(keys.get(choice), value);
        }
    }

    private void setProperty(String key, String value) {
        playground.getConfiguration().setProperty(key, value);
        if (!keys.contains(key)) {
            keys.add(key);
        }
    }

    private void createCollectible() {
        String collectibleName = console.readLine("Collectible id: ");
        String collectibles = playground.getConfiguration().getProperty("collectibles");
        if (collectibles == null) {
            setProperty("collectibles", collectibleName);
        } else {
            setProperty("collectibles", collectibles + "," + collectibleName);
        }

        String probability = console.readLine("Collectible appearence probability: ");
        setProperty("collectible.probability." + collectibleName, probability);
        String expiration = console.readLine("Collectible expiration: ");
        setProperty("collectible.expiration." + collectibleName, expiration);
        String price = console.readLine("Collectible price: ");
        setProperty("collectible.price." + collectibleName, price);
    }

}
