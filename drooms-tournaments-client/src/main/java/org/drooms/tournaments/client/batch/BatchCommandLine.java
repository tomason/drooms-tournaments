package org.drooms.tournaments.client.batch;

import static org.drooms.tournaments.client.common.ArgumentNames.BATCH;
import static org.drooms.tournaments.client.common.ArgumentNames.CREDENTIALS;
import static org.drooms.tournaments.client.common.ArgumentNames.HELP;
import static org.drooms.tournaments.client.common.ArgumentNames.SERVER;

import org.apache.commons.cli.Options;
import org.drooms.tournaments.client.common.AbstractArguments;

public class BatchCommandLine extends AbstractArguments {

    public BatchCommandLine(String[] args) {
        super(newOptions(), args);
    }

    private static Options newOptions() {
        Options result = new Options();

        BATCH.setRequired(true);
        SERVER.setRequired(true);
        CREDENTIALS.setRequired(true);

        result.addOption(HELP);
        result.addOption(BATCH);
        result.addOption(SERVER);
        result.addOption(CREDENTIALS);

        // many more to be yet added

        return result;
    }
}
