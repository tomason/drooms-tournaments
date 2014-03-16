package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

class TournamentDetailMenu extends BackReferrenceMenu {
    private final Tournament tournament;

    public TournamentDetailMenu(Console console, TournamentsServerClient client, Menu previous, Tournament tournament) {
        super(console, client, previous);
        this.tournament = tournament;
    }

    @Override
    protected String getHeadline() {
        return tournament.getName() + " detail";
    }

    @Override
    protected boolean allowMainMenu() {
        return true;
    }

    @Override
    protected void printMenu() {
        printTournamentDetail(console, tournament);
        if (!tournament.isEnrolled()) {
            console.format("1. join tournament%n");
        }

    }

    @Override
    protected Menu execute(int choice) {
        if (choice == 1 && !tournament.isEnrolled()) {
            client.joinTournament(tournament);
        }

        return back();
    }

    static void printTournamentDetail(Console console, Tournament tournament) {
        console.format("%s%n", SINGLE_LINE);
        console.format("| Name           | %-59s |%n", trimToSize(tournament.getName(), 59));
        console.format("| Start date     | %1$tY-%1$tm-%1$td%2$49s |%n", tournament.getStart(), "");
        console.format("| End date       | %1$tY-%1$tm-%1$td%2$49s |%n", tournament.getEnd(), "");
        console.format("| Period (hours) | %-59s |%n", tournament.getPeriod());
        console.format("| Enrolled       | %-59s |%n", tournament.isEnrolled() ? "yes" : "no");
        console.format("%s%n", SINGLE_LINE);
        console.format("| Playgrounds    |   | Name                                          | players |%n");
        console.format("|                |-------------------------------------------------------------|%n");
        for (int i = 1; i <= tournament.getPlaygrounds().size(); i++) {
            Playground p = tournament.getPlaygrounds().get(i - 1);
            console.format("|                |%3s| %45s | %-7s |%n", i, trimToSize(p.getName(), 45), p.getMaxPlayers());
        }
        console.format("%s%n", SINGLE_LINE);
    }
}
