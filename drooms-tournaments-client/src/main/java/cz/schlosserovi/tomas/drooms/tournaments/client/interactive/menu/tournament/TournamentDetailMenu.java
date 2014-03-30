package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament;

import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.services.TournamentService;

class TournamentDetailMenu extends BackReferrenceMenu {
    private final Tournament tournament;

    protected TournamentDetailMenu(OutputDevice console, TournamentsServerClient client, Menu previous, Tournament tournament) {
        super(console, client, previous);
        this.tournament = tournament;
    }

    @Override
    protected String getHeadline() {
        return tournament.getName() + " detail";
    }

    @Override
    protected Set<Choice> getChoices() {
        if (client.isLoggedIn() && !tournament.isEnrolled()) {
            return concatChoices(super.getChoices(), Choice.TOURNAMENT_JOIN);
        } else {
            return super.getChoices();
        }
    }

    @Override
    protected void printInstructions() {
        printTournamentDetail(console, tournament);
    }

    @Override
    protected Menu execute(Choice choice) {
        if (choice == Choice.TOURNAMENT_JOIN) {
            client.getService(TournamentService.class).joinTournament(tournament);
            return new TournamentsMenu(console, client);
        } else {
            return super.execute(choice);
        }
    }

    static void printTournamentDetail(OutputDevice console, Tournament tournament) {
        console.print("%s%n", SINGLE_LINE);
        console.print("| Name           | %-59s |%n", trimToSize(tournament.getName(), 59));
        console.print("| Start date     | %1$tY-%1$tm-%1$td%2$49s |%n", tournament.getStart(), "");
        console.print("| End date       | %1$tY-%1$tm-%1$td%2$49s |%n", tournament.getEnd(), "");
        console.print("| Period (hours) | %-59s |%n", tournament.getPeriod());
        console.print("| Enrolled       | %-59s |%n", tournament.isEnrolled() ? "yes" : "no");
        console.print("|------------------------------------------------------------------------------|%n");
        console.print("| Playgrounds    |   | Name                                          | players |%n");
        console.print("|                |-------------------------------------------------------------|%n");
        for (int i = 1; i <= tournament.getPlaygrounds().size(); i++) {
            Playground p = tournament.getPlaygrounds().get(i - 1);
            console.print("|                |%3s| %45s | %-7s |%n", i, trimToSize(p.getName(), 45), p.getMaxPlayers());
        }
        console.print("%s%n", SINGLE_LINE);
    }
}
