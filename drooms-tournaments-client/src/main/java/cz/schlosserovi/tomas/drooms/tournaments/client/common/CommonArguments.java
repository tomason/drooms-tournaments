package cz.schlosserovi.tomas.drooms.tournaments.client.common;

import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.BATCH;
import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.EXECUTION;
import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.HELP;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class CommonArguments extends AbstractArguments {

    public CommonArguments(String[] args) {
        super(newOptions(), args);
    }

    public boolean isBatchMode() {
        return commandLine.hasOption(BATCH.getOpt());
    }

    public boolean isExecutionMode() {
        return commandLine.hasOption(EXECUTION.getOpt());
    }

    private static Options newOptions() {
        OptionGroup group = new OptionGroup();
        group.addOption(BATCH);
        group.addOption(EXECUTION);

        Options result = new Options();
        result.addOptionGroup(group);
        result.addOption(HELP);

        return result;
    }
}
