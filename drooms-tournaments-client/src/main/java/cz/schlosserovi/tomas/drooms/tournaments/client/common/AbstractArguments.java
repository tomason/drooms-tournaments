package cz.schlosserovi.tomas.drooms.tournaments.client.common;

import static cz.schlosserovi.tomas.drooms.tournaments.client.common.ArgumentNames.HELP;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public abstract class AbstractArguments {
    private final Options options;
    private boolean help = false;
    private String helpMessage;

    protected final CommandLine commandLine;

    public AbstractArguments(Options options, String[] args) {
        CommandLine commandLine = null;
        try {
            CommandLineParser parser = new GnuParser();
            commandLine = parser.parse(options, args, true);

            help = commandLine.getOptions().length == 1 && commandLine.hasOption(HELP.getOpt());
        } catch (ParseException ex) {
            helpMessage = ex.getMessage();
            help = true;
        }

        this.options = options;
        this.commandLine = commandLine;
    }

    public void setHelpMessage(String helpMessage) {
        this.help = true;
        this.helpMessage = helpMessage;
    }

    public boolean isHelp() {
        return help;
    }

    protected String getCmdLineSyntax() {
        return "java -jar drooms-tournaments-client.jar";
    }

    protected String getHeader() {
        return null;
    }

    protected String getFooter() {
        return null;
    }

    public void printHelp() {
        if (helpMessage != null) {
            System.out.println(helpMessage);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getCmdLineSyntax(), getHeader(), options, getFooter(), true);
    }
}
