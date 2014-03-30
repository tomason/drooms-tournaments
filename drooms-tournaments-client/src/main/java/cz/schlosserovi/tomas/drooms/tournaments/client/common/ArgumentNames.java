package cz.schlosserovi.tomas.drooms.tournaments.client.common;

import org.apache.commons.cli.Option;

public class ArgumentNames {
    // client mode options
    public static final Option BATCH = new Option("C", "command", false, "Run the client in command mode.");
    public static final Option EXECUTION = new Option("E", "execution", false, "Run the client in execution mode.");
    public static final Option INTERACTIVE = new Option("I", "interactive", false, "Run the client in interactive mode.");

    public static final Option HELP = new Option("h", "help", false, "Prints help message.");
    public static final Option SERVER = new Option("s", "server", true, "Address of the Drooms tournaments server.");
    public static final Option CREDENTIALS = new Option("c", "credentials", true, "Credentials to use for server authentication in format Base64(username ':' password");
}
