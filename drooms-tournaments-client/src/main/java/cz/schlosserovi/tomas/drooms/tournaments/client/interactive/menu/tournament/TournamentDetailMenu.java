package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.domain.TournamentResult;
import cz.schlosserovi.tomas.drooms.tournaments.services.TournamentService;

class TournamentDetailMenu extends BackReferrenceMenu {
    private final Tournament tournament;

    protected TournamentDetailMenu(OutputDevice console, TournamentsServerClient client, Menu previous, Tournament tournament) {
        super(console, client, previous);
        this.tournament = client.getService(TournamentService.class).getTournament(tournament.getName());
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

    static void printTournamentDetail(OutputDevice out, Tournament tournament) {
        out.printLine(singleLine());
        out.printLine("| Name           | %-59s |", trimToSize(tournament.getName(), 59));
        out.printLine("| Start date     | %1$tY-%1$tm-%1$td%2$49s |", tournament.getStart(), "");
        out.printLine("| End date       | %1$tY-%1$tm-%1$td%2$49s |", tournament.getEnd(), "");
        out.printLine("| Period (hours) | %-59s |", tournament.getPeriod());
        out.printLine("|%s|", singleLine(78));
        out.printLine("| Playgrounds    |   | %45s | Players |", "Name");
        out.printLine("|                |%s|", singleLine(61));

        List<Playground> playgrounds = new LinkedList<>(tournament.getPlaygrounds());
        for (int i = 1; i <= playgrounds.size(); i++) {
            Playground p = playgrounds.get(i - 1);
            out.printLine("|                |%3s| %45s | %-7s |", i, trimToSize(p.getName(), 45), p.getMaxPlayers());
        }

        if (tournament.getResults() != null) {
            out.printLine("|%s|", singleLine(78));
            out.printLine("| Players        |   | %55s |", "Name");
            out.printLine("|                |%s|", singleLine(61));

            List<TournamentResult> results = new LinkedList<>(tournament.getResults());
            Collections.sort(results);
            for (TournamentResult result : results) {
                out.printLine("|                |%3s| %-55s |", result.getPosition(), result.getPlayer().getName());
            }
        }

        out.printLine("%s", singleLine());
    }
}
