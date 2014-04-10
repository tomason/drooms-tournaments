package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu;

import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.playground.PlaygroundsMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.strategy.StrategiesMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament.TournamentsMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;

public class MainMenu extends Menu {

    public MainMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "";
    }

    @Override
    protected Set<Choice> getChoices() {
        Set<Choice> choices = super.getChoices();

        // remove the main menu (it would not make much sense)
        choices.remove(Choice.MAIN_MENU);

        // add generally available menus
        concatChoices(choices, Choice.PLAYGROUNDS, Choice.TOURNAMENTS);

        // add specific menus
        if (client.isLoggedIn()) {
            concatChoices(choices, Choice.STRATEGIES);
        } else {
            concatChoices(choices, Choice.REGISTER);
        }

        return choices;
    }

    @Override
    protected void printInstructions() {
        // TODO how about some welcoming message?
    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case STRATEGIES:
            return new StrategiesMenu(console, client);
        case PLAYGROUNDS:
            return new PlaygroundsMenu(console, client);
        case TOURNAMENTS:
            return new TournamentsMenu(console, client);
        case REGISTER:
            return new RegisterMenu(console, client);
        default:
            return super.execute(choice);
        }
    }

}
