package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.services.TournamentService;

public class TournamentsMenu extends Menu {
    private List<Tournament> tournaments;

    public TournamentsMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "tournaments";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.TOURNAMENT_NEW, Choice.TOURNAMENT_DETAIL, Choice.TOURNAMENT_JOIN);
    }

    @Override
    protected void printInstructions() {
        console.print("List of %s's tournaments:%n", client.getLoogedInUser());
        console.print("%s%n", SINGLE_LINE);
        console.print("|   |                                  name |      start |        end | joined |%n");
        console.print("%s%n", SINGLE_LINE);
        tournaments = new LinkedList<>(client.getService(TournamentService.class).getUserTournaments());
        for (int i = 1; i <= tournaments.size(); i++) {
            Tournament t = tournaments.get(i - 1);
            console.print("|%3s", i);
            console.print("| %38s", trimToSize(t.getName(), 38));
            console.print("| %1$tY-%1$tm-%1$td ", t.getStart());
            console.print("| %1$tY-%1$tm-%1$td ", t.getEnd());
            console.print("| %6s |%n", t.isEnrolled() ? "yes" : "no");
        }
        console.print("%s%n", SINGLE_LINE);
    }

    @Override
    protected Menu execute(Choice choice) {
        int index;
        switch (choice) {
        case TOURNAMENT_NEW:
            return new NewTournamentMenu(console, client);
        case TOURNAMENT_DETAIL:
            index = parseChoice("Tournament index (see table above): ") - 1;
            if (index >= 0 && index < tournaments.size()) {
                return new TournamentDetailMenu(console, client, this, tournaments.get(index));
            }
            break;
        case TOURNAMENT_JOIN:
            index = parseChoice("Tournament index (see table above): ") - 1;
            if (index >= 0 && index < tournaments.size() && !tournaments.get(index).isEnrolled()) {
                client.getService(TournamentService.class).joinTournament(tournaments.get(index));
            }

            break;
        default:
            return super.execute(choice);
        }

        return this;
    }

}
