package cz.schlosserovi.tomas.drooms.tournaments.client.executor;

import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.CREDENTIALS;
import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.EXECUTION;
import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.HELP;
import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.SERVER;

import org.apache.commons.cli.Options;

import cz.schlosserovi.tomas.drooms.tournaments.client.common.AbstractArguments;

class ExecutorCommandLine extends AbstractArguments {
    public ExecutorCommandLine(String[] args) {
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

        EXECUTION.setRequired(true);
        SERVER.setRequired(true);
        CREDENTIALS.setRequired(true);

        result.addOption(HELP);
        result.addOption(EXECUTION);
        result.addOption(SERVER);
        result.addOption(CREDENTIALS);

        return result;
    }
}
