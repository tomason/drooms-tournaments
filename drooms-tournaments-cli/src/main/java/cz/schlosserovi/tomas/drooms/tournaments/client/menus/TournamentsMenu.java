package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

class TournamentsMenu extends Menu {
    private List<Tournament> tournaments;

    protected TournamentsMenu(Console console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "tournaments";
    }

    @Override
    protected boolean allowMainMenu() {
        return true;
    }

    @Override
    protected void printMenu() {
        console.format("List of %s's tournaments:%n", client.getLoogedInUser());
        console.format("%s%n", SINGLE_LINE);
        console.format("|   |                                  name |      start |        end | joined |%n");
        console.format("%s%n", SINGLE_LINE);
        tournaments = new LinkedList<>(client.getTournaments());
        for (int i = 1; i <= tournaments.size(); i++) {
            Tournament t = tournaments.get(i - 1);
            console.format("|%3s", i);
            console.format("| %38s", trimToSize(t.getName(), 38));
            console.format("| %1$tY-%1$tm-%1$td ", t.getStart());
            console.format("| %1$tY-%1$tm-%1$td ", t.getEnd());
            console.format("| %6s |%n", t.isEnrolled() ? "yes" : "no");
        }
        console.format("%s%n", SINGLE_LINE);
        console.format("1. new tournament%n");
        console.format("2. tournament detail%n");
        console.format("3. join tournament%n");
    }

    @Override
    protected Menu execute(int choice) {
        switch (choice) {
        case 1:
            return new NewTournamentMenu(console, client);
        case 2: {
            int index = parseChoice("Tournament index (see table above): ") - 1;
            if (index >= 0 && index < tournaments.size()) {
                return new TournamentDetailMenu(console, client, this, tournaments.get(index));
            }
        } break;
        case 3: {
            int index = parseChoice("Tournament index (see table above): ") - 1;
            if (index >= 0 && index < tournaments.size() && !tournaments.get(index).isEnrolled()) {
                client.joinTournament(tournaments.get(index));
            }
        } break;
        }

        return new TournamentsMenu(console, client);
    }

}
