package org.drooms.tournaments.client.interactive.menu.tournament;

import java.util.Set;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.services.TournamentService;

class TournamentConfirmationMenu extends Menu {
    private final Tournament tournament;

    protected TournamentConfirmationMenu(OutputDevice console, TournamentsServerClient client, Tournament tournament) {
        super(console, client);
        this.tournament = tournament;
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.TOURNAMENT_CREATE, Choice.TOURNAMENT_DISCARD);
    }

    @Override
    protected void printInstructions() {
        TournamentDetailMenu.printTournamentDetail(console, tournament);
    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case TOURNAMENT_CREATE:
            client.getService(TournamentService.class).newTournament(tournament);
        case TOURNAMENT_DISCARD:
            return new TournamentsMenu(console, client);
        default:
            return super.execute(choice);
        }
    }

}
