package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.Arrays;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;

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

    protected final Console console;
    protected final TournamentsServerClient client;

    public static Menu getMainMenu(Console console, TournamentsServerClient client) {
        return new MainMenu(console, client);
    }

    protected Menu(Console console, TournamentsServerClient client) {
        this.console = console;
        this.client = client;
    }

    public final Menu show() {
        printHeader(getHeadline());

        if (showMenu()) {
            printMenu();
            console.format("%n");
            printBasicChoices();

            int choice = parseChoice("Choose an action: ");

            Menu basic = executeBasicChoices(choice);
            return basic != null ? basic : execute(choice);
        }

        return execute(-1);
    }

    protected abstract String getHeadline();

    protected abstract void printMenu();

    protected abstract Menu execute(int choice);

    protected abstract boolean allowMainMenu();

    protected void printBasicChoices() {
        if (allowMainMenu()) {
            console.format("7. return to main menu%n");
        }
        if (client.isLoggedIn()) {
            console.format("8. logout%n");
        }
        console.format("9. exit%n");
    }

    protected Menu executeBasicChoices(int choice) {
        if (allowMainMenu() && choice == 7) {
            return new MainMenu(console, client);
        }
        if (client.isLoggedIn() && choice == 8) {
            return logout();
        }
        if (choice == 9) {
            return exit();
        }

        return null;
    }
    protected boolean showMenu() {
        return true;
    }

    protected void printHeader(String header) {
        if (header.length() > 0) {
            header = " - " + header;
        }
        console.format("%s%n", DOUBLE_LINE);
        console.format("| Drooms tournaments client%-51s |%n", header);
        console.format("| %76s |%n", "Logged in as: " + client.getLoogedInUser());
        console.format("%s%n%n", DOUBLE_LINE);
    }

    protected static String trimToSize(String orig, int length) {
        if (orig.length() <= length) {
            return orig;
        } else {
            return orig.substring(0, length);
        }
    }

    protected Menu logout() {
        client.logout();
        return new MainMenu(console, client);
    }

    protected Menu exit() {
        return new ExitMenu();
    }

    protected int parseChoice(String line) {
        while (true) {
            String choiceLine = console.readLine(line);
            try {
                int choice = Integer.parseInt(choiceLine);
                return choice;
            } catch (NumberFormatException ex) {
                console.format("Wrong input (%s).%n", choiceLine);
            }
        }
    }
}
