package org.drooms.tournaments.client.interactive.menu.tournament;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.FormMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Tournament;

class NewTournamentMenu extends FormMenu {
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Tournament tournament;

    protected NewTournamentMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
        tournament = new Tournament();
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected void printInstructions() {
        // TODO some info about new tournament
    }

    @Override
    protected Menu execute(Choice choice) {
        tournament.setName(console.readLine("Name: "));

        Calendar start = Calendar.getInstance();
        start.clear();
        while(true) {
            String startLine = console.readLine("Start date (yyyy-MM-dd): ");
            try {
                start.setTime(FORMAT.parse(startLine));
                break;
            } catch (ParseException ex) {
                console.print("Wrong input (%s) use yyyy-MM-dd format.%n", startLine);
            }
        }
        tournament.setStart(start);

        Calendar end = Calendar.getInstance();
        end.clear();
        while (true) {
            String endLine = console.readLine("End date (yyyy-MM-dd): ");
            try {
                end.setTime(FORMAT.parse(endLine));
                break;
            } catch (ParseException ex) {
                console.print("Wrong input (%s) use yyyy-MM-dd format.%n", endLine);
            }
        }
        tournament.setEnd(end);

        int period;
        while (true) {
            String periodLine = console.readLine("Scheduling period in hours: ");
            try {
                period = Integer.parseInt(periodLine);
                break;
            } catch (NumberFormatException ex) {
                console.print("Wrong input (%s).%n", periodLine);
            }
        }
        tournament.setPeriod(period);

        return new TournamentPlaygroundSelectionMenu(console, client, tournament);
    }

}
