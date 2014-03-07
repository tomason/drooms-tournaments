package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.Arrays;

import cz.schlosserovi.tomas.drooms.tournaments.client.UserServiceClient;

public abstract class Menu {
    protected static final String SINGLE_LINE;

    static {
        char[] singleLine = new char[80];
        Arrays.fill(singleLine, '-');
        SINGLE_LINE = new String(singleLine);
    }

    protected final Console console;
    protected final UserServiceClient client;

    public static Menu getMainMenu(Console console, UserServiceClient client) {
        return new MainMenu(console, client);
    }

    protected Menu(Console console, UserServiceClient client) {
        this.console = console;
        this.client = client;
    }

    public final Menu show() {
        printHeader(getHeadline());

        if (showMenu()) {
            printMenu();
            console.format("%n");
            if (allowMainMenu()) {
                console.format("7. return to main menu%n");
            }
            if (client.isLoggedIn()) {
                console.format("8. logout%n");
            }
            console.format("9. exit%n");

            int choice = parseChoice("Choose an action: ");
            if (allowMainMenu() && choice == 7) {
                return new MainMenu(console, client);
            }
            if (client.isLoggedIn() && choice == 8) {
                return logout();
            }
            if (choice == 9) {
                return exit();
            }

            return execute(choice);
        }

        return execute(-1);
    }

    protected abstract String getHeadline();

    protected abstract void printMenu();

    protected abstract Menu execute(int choice);

    protected abstract boolean allowMainMenu();

    protected boolean showMenu() {
        return true;
    }

    protected void printHeader(String header) {
        if (header.length() > 0) {
            header = " - " + header;
        }
        console.format("Drooms tournaments client %-24s %29s%n", header, client.getLoogedInUser());
        console.format("%s%n", SINGLE_LINE);
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
        return null;
    }

    protected int parseChoice(String line) {
        try {
            int choice = Integer.parseInt(console.readLine(line));
            return choice;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
