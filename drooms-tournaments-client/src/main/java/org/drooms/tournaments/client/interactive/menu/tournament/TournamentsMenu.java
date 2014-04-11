package org.drooms.tournaments.client.interactive.menu.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.services.TournamentService;

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
        if (client.isLoggedIn()) {
            return concatChoices(super.getChoices(), Choice.TOURNAMENT_NEW, Choice.TOURNAMENT_DETAIL, Choice.TOURNAMENT_JOIN);
        } else {
            return concatChoices(super.getChoices(), Choice.TOURNAMENT_DETAIL);
        }
    }

    @Override
    protected void printInstructions() {
        String caption, header;
        int nameWidth;
        if (client.isLoggedIn()) {
            caption = String.format("List of %s's tournaments:", client.getLoogedInUser());
            header = String.format("|   | %37s |      start |        end | joined |", "name");
            nameWidth = 38;
            tournaments = new LinkedList<>(client.getService(TournamentService.class).getUserTournaments()); 
        } else {
            caption = "List of tournaments:";
            header = String.format("|   | %46s |      start |        end |", "name");
            nameWidth = 47;
            tournaments = new LinkedList<>(client.getService(TournamentService.class).getTournaments());
        }

        console.printLine(caption);
        console.printLine(singleLine());
        console.printLine(header);
        console.printLine(singleLine());
        for (int i = 1; i <= tournaments.size(); i++) {
            Tournament t = tournaments.get(i - 1);
            console.print("|%3s", i);
            console.print("| %" + nameWidth + "s", trimToSize(t.getName(), 47));
            console.print("| %1$tY-%1$tm-%1$td ", t.getStart());
            console.print("| %1$tY-%1$tm-%1$td ", t.getEnd());
            if (client.isLoggedIn()) {
                console.printLine("| %6s |", t.isEnrolled() ? "yes" : "no");
            } else {
                console.printLine("|");
            }
        }
        console.printLine(singleLine());
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
