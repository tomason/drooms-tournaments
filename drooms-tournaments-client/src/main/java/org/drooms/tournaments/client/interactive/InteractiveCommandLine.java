package org.drooms.tournaments.client.interactive;

import static org.drooms.tournaments.client.common.ArgumentNames.CREDENTIALS;
import static org.drooms.tournaments.client.common.ArgumentNames.HELP;
import static org.drooms.tournaments.client.common.ArgumentNames.INTERACTIVE;
import static org.drooms.tournaments.client.common.ArgumentNames.SERVER;

import org.apache.commons.cli.Options;
import org.drooms.tournaments.client.common.AbstractArguments;

public class InteractiveCommandLine extends AbstractArguments {

    public InteractiveCommandLine(String[] args) {
        super(newOptions(), args);
    }

    public String getServer() {
        return commandLine.getOptionValue(SERVER.getOpt());
    }

    public String getCredentials() {
        return commandLine.getOptionValue(CREDENTIALS.getOpt());
    }

    private static Options newOptions() {
        Options result = new Options();

        INTERACTIVE.setRequired(false);
        SERVER.setRequired(false);
        CREDENTIALS.setRequired(false);

        result.addOption(HELP);
        result.addOption(INTERACTIVE);
        result.addOption(SERVER);
        result.addOption(CREDENTIALS);

        return result;
    }
}
