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
        this.tournament = loadTournament(tournament.getName());
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
        out.printLine("| Playgrounds    | %49s | Players |", "Name");
        out.printLine("|                |%s|", singleLine(61));

        for (Playground p : getPlaygrounds(tournament)) {
            out.printLine("|                | %49s | %-7s |", trimToSize(p.getName(), 49), p.getMaxPlayers());
        }

        List<TournamentResult> results = getResults(tournament);
        if (results.size() > 0) {
            out.printLine("|%s|", singleLine(78));
            out.printLine("| Players        |   | %55s |", "Name");
            out.printLine("|                |%s|", singleLine(61));

            for (TournamentResult result : results) {
                out.printLine("|                |%3s| %-55s |", result.getPosition(), result.getPlayer().getName());
            }
        }

        out.printLine("%s", singleLine());
    }

    private Tournament loadTournament(String name) {
        Tournament tournament = client.getService(TournamentService.class).getTournament(name);

        if (client.isLoggedIn()) {
            for (TournamentResult result : tournament.getResults()) {
                if (client.getLoogedInUser().equals(result.getPlayer().getName())) {
                    tournament.setEnrolled(true);
                    break;
                }
            }
        }

        return tournament;
    }

    private static List<Playground> getPlaygrounds(Tournament tournament) {
        List<Playground> playgrounds = new LinkedList<>(tournament.getPlaygrounds());
        Collections.sort(playgrounds);

        return playgrounds;
    }

    private static List<TournamentResult> getResults(Tournament tournament) {
        List<TournamentResult> results = new LinkedList<>();

        if (tournament.getResults() != null) {
            results.addAll(tournament.getResults());
        }

        Collections.sort(results);
        return results;
    }
}
