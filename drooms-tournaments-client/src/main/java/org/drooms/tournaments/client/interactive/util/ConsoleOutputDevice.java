package org.drooms.tournaments.client.interactive.util;

import java.io.Console;

public class ConsoleOutputDevice implements OutputDevice {
    private final Console console = System.console();

    @Override
    public void print(String format, Object... arguments) {
        console.format(format, arguments);
    }
    @Override
    public void printLine() {
        console.format("%n");
    }

    @Override
    public void printLine(String format, Object... arguments) {
        console.format(format + "%n", arguments);
    }

    @Override
    public String readLine() {
        return console.readLine();
    }

    @Override
    public String readLine(String format, Object... arguments) {
        return console.readLine(format, arguments);
    }

    @Override
    public String readPassword() {
        return new String(console.readPassword());
    }

    @Override
    public String readPassword(String format, Object... arguments) {
        return new String(console.readPassword(format, arguments));
    }

}
