package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.ExitMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.LoginMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.MainMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;

public abstract class Menu {
    protected static final String SINGLE_LINE;
    protected static final String DOUBLE_LINE;

    static {
        char[] singleLine = new char[80];
        Arrays.fill(singleLine, '-');
        SINGLE_LINE = new String(singleLine);

        char[] doubleLine = new char[80];
        Arrays.fill(doubleLine, '=');
        DOUBLE_LINE = new String(doubleLine);
    }

    protected final OutputDevice console;
    protected final TournamentsServerClient client;

    protected Menu(OutputDevice console, TournamentsServerClient client) {
        this.console = console;
        this.client = client;
    }

    public final Menu show() {
        printHeader(getHeadline());

        printInstructions();

        Menu result;
        Set<Choice> originalChoices = getChoices();
        if (originalChoices != null) {
            List<Choice> choices = new LinkedList<>(originalChoices);
            Collections.sort(choices);

            for (int i = 1; i <= choices.size(); i++) {
                console.printLine("%s) %s", i, choices.get(i - 1).getDescription());
            }

            int choiceIndex = parseChoice("Choose an action: ");
            if (choiceIndex < 1 || choiceIndex > choices.size()) {
                // invalid index given
                result = this;
            } else {
                result = execute(choices.get(choiceIndex - 1));
            }
        } else {
            // no choices were given
            result = execute(null);
        }

        return result;
    }

    protected abstract String getHeadline();

    protected void printInstructions() {
        // print no instructions by default
    }

    protected Set<Choice> getChoices() {
        Set<Choice> result = new HashSet<>();

        if (client.isLoggedIn()) {
            result.add(Choice.LOGOUT);
        } else {
            result.add(Choice.LOGIN);
        }

        result.add(Choice.MAIN_MENU);
        result.add(Choice.QUIT);

        return result;
    }

    protected Menu execute(Choice choice) {
        // handle Basic options
        switch (choice) {
        case LOGIN:
            return new LoginMenu(console, client);
        case LOGOUT:
            client.logout();
        case MAIN_MENU:
            return new MainMenu(console, client);
        case QUIT:
            return new ExitMenu();
        default:
            return this;
        }
    }

    protected static String trimToSize(String orig, int length) {
        if (orig.length() <= length) {
            return orig;
        } else {
            return orig.substring(0, length);
        }
    }

    protected Set<Choice> concatChoices(Set<Choice> original, Choice... newChoices) {
        original.addAll(Arrays.asList(newChoices));
        return original;
    }

    protected int parseChoice(String line) {
        while (true) {
            String choiceLine = console.readLine(line);
            try {
                int choice = Integer.parseInt(choiceLine);
                return choice;
            } catch (NumberFormatException ex) {
                console.printLine("Wrong input (%s).", choiceLine);
            }
        }
    }

    private void printHeader(String header) {
        if (header.length() > 0) {
            header = " - " + header;
        }
        console.printLine(DOUBLE_LINE);
        console.printLine("| Drooms tournaments client%-51s |", header);
        console.printLine("| %76s |", "Logged in as: " + client.getLoogedInUser());
        console.printLine(DOUBLE_LINE);
        console.printLine();
    }

}
