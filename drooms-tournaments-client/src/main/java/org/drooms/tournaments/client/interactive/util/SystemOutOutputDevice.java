package org.drooms.tournaments.client.interactive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class SystemOutOutputDevice implements OutputDevice {
    private final PrintStream output;
    private final BufferedReader input;

    public SystemOutOutputDevice() {
        output = System.out;
        input = new BufferedReader(new InputStreamReader(System.in));

        output.println("********************************************************************************");
        output.println("*                                 W A R N I N G                                *");
        output.println("*                                                                              *");
        output.println("*   You are currently using console that uses System.out and System.in         *");
        output.println("*   for output and input. This poses a danger as the passwords you put in are  *");
        output.println("*   visible. This implementation should only be used for testing.              *");
        output.println("*                                                                              *");
        output.println("*   If you are running from your IDE consider switching to distributed binary. *");
        output.println("*                                                                              *");
        output.println("********************************************************************************");
    }

    @Override
    public void print(String format, Object... arguments) {
        output.print(String.format(format, arguments));
    }

    @Override
    public void printLine() {
        output.println();
    }

    @Override
    public void printLine(String format, Object... arguments) {
        output.println(String.format(format, arguments));
    }

    @Override
    public String readLine() {
        try {
            return input.readLine();
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read input", ex);
        }
    }

    @Override
    public String readLine(String format, Object... arguments) {
        print(format, arguments);
        return readLine();
    }

    @Override
    public String readPassword() {
        return readLine();
    }

    @Override
    public String readPassword(String format, Object... arguments) {
        return readLine(format, arguments);
    }

}
