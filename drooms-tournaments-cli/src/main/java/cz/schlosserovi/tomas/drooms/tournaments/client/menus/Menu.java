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

    public abstract Menu show();

    protected static void printHeader(UserServiceClient client, Console console, String header) {
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
}
